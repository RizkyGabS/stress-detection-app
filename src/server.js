const express = require('express');
const bodyParser = require('body-parser');
const { Datastore } = require('@google-cloud/datastore');
const session = require('express-session');
const bcrypt = require('bcrypt');
const path = require('path');
const cors = require('cors');

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

// Route default diarahkan ke halaman login
app.get('/', (req, res) => {
  res.redirect('/login');
});

// Route untuk halaman home
app.get('/home', async (req, res) => {
  try {
    // Mengambil data pengguna yang sesuai dengan session user ID
    const userId = req.session.userId;
    const key = datastore.key([KIND, userId]);
    const [user] = await datastore.get(key);

    res.render('home', { naming: user.nama }); // Rendering halaman 'home' dengan nama pengguna
  } catch (err) {
    console.error('Error:', err);
    res.status(500).send('Server Error');
  }
});
app.get('/register', (req, res) => {
    res.render('register')
})
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

    // Pengguna diarahkan ke halaman success_regis jika registrasi berhasil dilakukan
    res.render('success_regis');
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

    // Redirect to '/home'
    res.redirect('/home');
  } catch (error) {
    console.error('Error:', error);
    return res.status(500).json({ message: 'Terjadi kesalahan saat login' });
  }
});


// Memulai server
app.listen(PORT, () => {
  console.log(`Server berjalan di http://localhost:${PORT}`);
});
