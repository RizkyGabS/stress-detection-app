const express = require('express');
const bodyParser = require('body-parser');
const mongoose = require('mongoose');
const cors = require('cors');
const bcrypt = require('bcrypt');
const path = require('path');
const session = require('express-session');
// const hbs  = require('hbs');

const app = express();
const PORT = process.env.PORT || 8080;
app.use(cors());
app.use(express.json());    

app.use(express.urlencoded({ extended: false }))

const tempelatePath = path.join(__dirname, '../src/views')
const publicPath = path.join(__dirname, '../src/public')
console.log(publicPath);

app.set('view engine', 'hbs')
app.set('views', tempelatePath)
app.use(express.static(publicPath))

// Set up express-session middleware
app.use(session({
    secret: 'secret-key',
    resave: false,
    saveUninitialized: true
}));

// Koneksi ke MongoDB (pastikan MongoDB sudah berjalan)
mongoose.connect('mongodb://127.0.0.1:27017/user_database');

// Membuat skema user untuk koleksi users di MongoDB
const userSchema = new mongoose.Schema({
    nama: String,
    email: String,
    password: String,
});

const User = mongoose.model('User', userSchema);

// Middleware untuk parsing body dalam format JSON
app.use(bodyParser.json());

// Route default diarahkan ke halaman login
app.get('/', (req, res) => {
    res.redirect('/login');
});
// Route untuk halaman home
app.get('/home', async (req, res) => {
    try {
        // Mengambil data pengguna yang sesuai dengan session user ID
        const userId = req.session.userId;
        const user = await User.findById(userId);

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

// Endpoint untuk register
app.post('/register', async (req, res) => {
    const { nama, email, password } = req.body;

    try {
        // Periksa apakah email sudah terdaftar sebelumnya
        const existingUser = await User.findOne({ email });

        if (existingUser) {
            return res.status(400).json({ message: 'Email sudah terdaftar' });
        }

        // Hash password sebelum menyimpan ke database
        const hashedPassword = await bcrypt.hash(password, 10);

        // Buat pengguna baru dengan password yang di-hash
        const newUser = new User({ nama, email, password: hashedPassword });
        await newUser.save();

        // Pengguna diarahkan ke halaman success_regis jika registrasi berhasil dilakukan
        res.render('success_regis')
    } catch (error) {
        return res.status(500).json({ message: 'Terjadi kesalahan saat registrasi' });
    }
});

// Endpoint untuk login
app.post('/login', async (req, res) => {
    const { email, password } = req.body;

    try {
        // Cari pengguna berdasarkan email
        const user = await User.findOne({ email });

        if (!user) {
            return res.status(404).json({ message: 'Email tidak ditemukan' });
        }

        // Verifikasi password menggunakan bcrypt
        const passwordMatch = await bcrypt.compare(password, user.password);

        if (!passwordMatch) {
            return res.status(401).json({ message: 'Password salah' });
        }

        // Jika email dan password sesuai, kirim respons berhasil
        // return res.status(200).json({ message: 'Login berhasil' });

        // Set session with user ID after successful login
        req.session.userId = user._id;

        // Redirect to '/'
        res.redirect('/home');
        
    } catch (error) {
        return res.status(500).json({ message: 'Terjadi kesalahan saat login' });
    }
});

// Memulai server
app.listen(PORT, () => {
    console.log(`Server berjalan di http://localhost:${PORT}`);
});