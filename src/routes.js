const express = require('express');
const router = express.Router();
const {
    showName,
    registerUser,
    loginUser,
    logoutUser,
    addNewHistory,
    getAllHistories,
    getHistoryDetail
} = require('./controllers');

// Add your Datastore setup/import here
const { Datastore } = require('@google-cloud/datastore');
const datastore = new Datastore();
const KIND = 'User'; // Adjust this to your Datastore entity kind

// Middleware for authentication
const authenticationMiddleware = async (req, res, next) => {
    try {
        const userId = req.session.userId;

        if (!userId) {
            return res.status(401).send('Unauthorized');
        }

        // Check if the user exists in Datastore
        const userKey = datastore.key([KIND, userId]);
        const [user] = await datastore.get(userKey);

        if (!user) {
            return res.status(401).send('Unauthorized');
        }

        // If authentication succeeds, proceed to the next middleware/controller
        next();
    } catch (error) {
        console.error('Authentication error:', error);
        res.status(500).send('Server Error');
    }
};

router.get('/', (req, res) => {
    res.redirect('/login');
});

router.get('/home', authenticationMiddleware, showName);

router.get('/register', (req, res) => {
    res.render('register');
});

router.get('/login', (req, res) => {
    res.render('login');
});

router.get('/history', (req, res) => {
    res.render('history');
});

router.post('/register', registerUser(datastore, KIND)); // Pass Datastore instance and entity kind
router.post('/login', loginUser(datastore, KIND, /* Add bcrypt for password verification if needed */));
router.get('/logout', logoutUser);
router.post('/addnewhistory', addNewHistory);
router.get('/getAllhistories', getAllHistories);
router.get('/detail/:historyId', getHistoryDetail);

module.exports = router;
