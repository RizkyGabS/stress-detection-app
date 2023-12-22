const express = require('express');
const bodyParser = require('body-parser');
const { Datastore } = require('@google-cloud/datastore');
const session = require('express-session');
const bcrypt = require('bcrypt');
const path = require('path');
const cors = require('cors');
const multer = require('multer');

const app = express();
const PORT = process.env.PORT || 8080;
app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: false }));

const tempelatePath = path.join(__dirname, '../src/views');
const publicPath = path.join(__dirname, '../src/public');
console.log(publicPath);

app.set('view engine', 'hbs');
app.set('views', tempelatePath);
app.use(express.static(publicPath));

// Set up express-session middleware
app.use(
  session({
    secret: 'secret-key',
    resave: false,
    saveUninitialized: true,
  })
);

// Connect to Google Cloud Datastore
const datastore = new Datastore();

// User entity kind
const KIND = 'User';

// Setup Multer for file upload
const storage = multer.memoryStorage();
const upload = multer({ storage: storage });

// Route default diarahkan ke halaman login
app.get('/', (req, res) => {
  res.redirect('/login');
});
  
app.get('/login', (req, res) => {
    res.render('login')
})

// ... Other routes ...

// Endpoint untuk register
app.post('/register', async (req, res) => {
    const { nama, email, password } = req.body;
  
    try {
      // Check if the user already exists
      const query = datastore.createQuery(KIND).filter('email', '=', email);
      const [existingUser] = await datastore.runQuery(query);
  
      if (existingUser && existingUser.length > 0) {
        return res.status(400).json({ message: 'Email sudah terdaftar' });
      }
  
      // Hash password before storing in Datastore
      const hashedPassword = await bcrypt.hash(password, 10);
  
      // Create a new user entity
      const newUser = {
        nama,
        email,
        password: hashedPassword,
      };
  
      // Save user entity to Datastore
      const userKey = datastore.key(KIND);
      await datastore.save({ key: userKey, data: newUser });
  
      // Kirim respons JSON untuk sukses registrasi dengan format yang diminta
      const registerResult = {
        email,
        name: nama,
      };
  
      res.status(201).json({ message: 'success', registerResult });
    } catch (error) {
      console.error('Error:', error);
      return res.status(500).json({ message: 'Terjadi kesalahan saat registrasi' });
    }
  });
  

// Endpoint untuk login
app.post('/login', async (req, res) => {
    const { email, password } = req.body;
  
    try {
      // Find the user by email
      const query = datastore.createQuery(KIND).filter('email', '=', email);
      const [users] = await datastore.runQuery(query);
  
      if (!users || users.length === 0) {
        return res.status(404).json({ message: 'Email tidak ditemukan' });
      }
  
      const user = users[0];
      // Verifikasi password menggunakan bcrypt
      const passwordMatch = await bcrypt.compare(password, user.password);
  
      if (!passwordMatch) {
        return res.status(401).json({ message: 'Password salah' });
      }
  
      // Set session with user ID after successful login
      req.session.userId = user[datastore.KEY].id;
  
      // Prepare the login result data including userId
      const loginResult = {
        userId: user[datastore.KEY].id, // Include userId in the login result
        email: user.email,
        name: user.nama // Assuming 'nama' is the user's name field
        // You can include any other necessary user information here except for sensitive data like password
      };
  
      // Send the success message along with the login result data in the JSON response
      res.status(200).json({ message: 'success', loginResult });
    } catch (error) {
      console.error('Error:', error);
      return res.status(500).json({ message: 'Terjadi kesalahan saat login' });
    }
  });   

// Endpoint untuk logout
app.post('/logout', (req, res) => {
    try {
      req.session.destroy((err) => {
        if (err) {
          console.error('Error destroying session:', err);
          return res.status(500).json({ message: 'Failed to logout' });
        }
        res.clearCookie('connect.sid'); // Clear session cookie
        res.status(200).json({ message: 'Logout successful' }); // Respond with a success message after logout
      });
    } catch (error) {
      console.error('Error:', error);
      return res.status(500).json({ message: 'An error occurred during logout' });
    }
  });

// Endpoint untuk menambahkan history baru dengan input gambar, nama, dan email
app.post('/addhistory', upload.single('image'), async (req, res) => {
  const { facialEmotion, stressLevel } = req.body;
  const userId = req.session.userId; // Dapatkan userId dari session
  const { originalname } = req.file; // File gambar yang diunggah

  try {
      // Handling the image data as per your requirement
      // For example, you can save the image to Cloud Storage or process it as needed
      
      // Create a new history entity
      const historyKey = datastore.key(['User', userId, 'History']);

      // Create a new history entry
      const newHistory = {
          key: historyKey,
          data: [
              {
                  name: 'parent',
                  value: datastore.key(['User', userId]),
                  excludeFromIndexes: true,
              },
              {
                  name: 'userId',
                  value: userId,
                  excludeFromIndexes: true,
              },
              {
                  name: 'photoUrl',
                  value: originalname, // Example: storing the original image name
                  excludeFromIndexes: true,
              },
              {
                  name: 'facialEmotion',
                  value: facialEmotion,
                  excludeFromIndexes: true,
              },
              {
                  name: 'stressLevel',
                  value: stressLevel,
                  excludeFromIndexes: true,
              },
              {
                  name: 'createdAt',
                  value: new Date(),
                  excludeFromIndexes: true,
              },
          ],
      };

      // Save the new entry to Datastore
      await datastore.save(newHistory);

      res.status(201).json({ message: 'Successfully added to history', history: newHistory });
  } catch (error) {
      console.error('Error adding new history:', error);
      return res.status(500).json({ message: 'An error occurred while adding new history' });
  }
});

// Endpoint untuk mendapatkan semua histories
app.get('/histories', async (req, res) => {
  const userId = req.session.userId;

  try {
    const query = datastore
      .createQuery('History')
      .hasAncestor(datastore.key(['User', userId]));

    const [histories] = await datastore.runQuery(query);

    res.status(200).json({ message: 'All histories fetched successfully' , histories });
  } catch (error) {
    console.error('Error fetching history:', error); // Menampilkan pesan kesalahan ke konsol

    // Mengirimkan pesan kesalahan sebagai respons JSON
    return res.status(500).json({ message: 'An error occurred while fetching history', error: error.message });
  }
});

// Endpoint untuk mendapatkan detail history
app.get('/history/:historyId', async (req, res) => {
  const { historyId } = req.params;

  try {
    // Temukan detail history berdasarkan ID
    const historyKey = datastore.key(['User', req.session.userId, 'History', parseInt(historyId, 10)]);
    const [history] = await datastore.get(historyKey);

    if (!history) {
      return res.status(404).json({ message: 'History not found' });
    }

    // Menambahkan ID history ke dalam data respons
    history.id = historyKey.id;

    res.status(200).json({ message: 'Story details fetched successfully', history });
  } catch (error) {
    return res.status(500).json({ message: 'An error occurred while fetching history detail' });
  }
});

// Memulai server
app.listen(PORT, () => {
  console.log(`Server berjalan di http://localhost:${PORT}`);
});
