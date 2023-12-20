const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const session = require('express-session');
const path = require('path');
const routes = require('./routes');

const app = express();
const PORT = process.env.PORT || 3000;

app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: false }));

app.use(session({
    secret: 'secret-key',
    resave: false,
    saveUninitialized: true
}));

// If previously connecting to MongoDB, you can remove the Mongoose-related code
// mongoose.connect('mongodb://127.0.0.1:27017/user_database');

const tempelatePath = path.join(__dirname, '../src/views');
const publicPath = path.join(__dirname, '../src/public');
app.set('view engine', 'hbs');
app.set('views', tempelatePath);
app.use(express.static(publicPath));

app.use('/', routes);

app.listen(PORT, () => {
    console.log(`Server berjalan di http://localhost:${PORT}`);
});
