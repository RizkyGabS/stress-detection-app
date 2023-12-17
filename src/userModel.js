const mongoose = require('mongoose');

const userSchema = new mongoose.Schema({
    nama: { type: String, required: true, unique: false },
    email: { type: String, required: true, unique: true },
    password: { type: String, required: true },
});

const User = mongoose.model('User', userSchema);

module.exports = User;