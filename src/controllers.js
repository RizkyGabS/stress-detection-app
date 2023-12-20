const bcrypt = require('bcrypt');
const { Datastore } = require('@google-cloud/datastore');
const { v4: uuidv4 } = require('uuid');
const datastore = new Datastore();

async function showName(req, res) {
    try {
        const userId = req.session.userId;

        const userKey = datastore.key(['User', userId]);
        const [user] = await datastore.get(userKey);

        if (!user) {
            return res.status(404).send('User not found');
        }

        res.render('home', { naming: user.nama });
    } catch (err) {
        console.error('Error:', err);
        res.status(500).send('Server Error');
    }
}

async function registerUser(req, res) {
    const { nama, email, password } = req.body;

    try {
        const query = datastore.createQuery('User').filter('email', '=', email);
        const [existingUser] = await datastore.runQuery(query);

        if (existingUser && existingUser.length > 0) {
            return res.status(400).json({ message: 'Email sudah terdaftar' });
        }

        const hashedPassword = await bcrypt.hash(password, 10);

        const newUser = {
            nama,
            email,
            password: hashedPassword,
        };

        const userKey = datastore.key('User');
        await datastore.save({ key: userKey, data: newUser });

        res.render('success_regis');
    } catch (error) {
        console.error('Error:', error);
        return res.status(500).json({ message: 'Terjadi kesalahan saat registrasi' });
    }
}

async function loginUser(req, res) {
    const { email, password } = req.body;

    try {
        const query = datastore.createQuery('User').filter('email', '=', email);
        const [users] = await datastore.runQuery(query);

        if (!users || users.length === 0) {
            return res.status(404).json({ message: 'Email tidak ditemukan' });
        }

        const user = users[0];
        const passwordMatch = await bcrypt.compare(password, user.password);

        if (!passwordMatch) {
            return res.status(401).json({ message: 'Password salah' });
        }

        req.session.userId = user[datastore.KEY].id;
        res.redirect('/home');
    } catch (error) {
        console.error('Error:', error);
        return res.status(500).json({ message: 'Terjadi kesalahan saat login' });
    }
}

async function logoutUser(req, res) {
    try {
        req.session.destroy((err) => {
            if (err) {
                console.error('Error logging out:', err);
                return res.status(500).json({ message: 'Gagal logout' });
            }
            res.redirect('/login');
        });
    } catch (error) {
        console.error('Error logging out:', error);
        return res.status(500).json({ message: 'Terjadi kesalahan saat logout' });
    }
}

async function addNewHistory(req, res) {
    const { result } = req.body;
    const userId = req.session.userId;

    try {
        const newHistory = {
            userId,
            result,
            createdAt: new Date().toISOString(),
            historyId: uuidv4(),
        };

        const historyKey = datastore.key('History');
        await datastore.save({ key: historyKey, data: newHistory });

        res.status(201).json({ message: 'Successfully added to history' });
    } catch (error) {
        console.error('Error adding new history:', error);
        return res.status(500).json({ message: 'An error occurred while adding new history' });
    }
}

async function getAllHistories(req, res) {
    const userId = req.session.userId;

    try {
        const query = datastore.createQuery('History').filter('userId', '=', userId);
        const [histories] = await datastore.runQuery(query);

        const historiesWithCreatedAt = histories.map(history => ({
            ...history,
            createdAt: history.createdAt.toISOString(),
        }));

        res.status(200).json({ histories: historiesWithCreatedAt });
    } catch (error) {
        return res.status(500).json({ message: 'Terjadi kesalahan saat mengambil history' });
    }
}

async function getHistoryDetail(req, res) {
    const { historyId } = req.params;

    try {
        const historyKey = datastore.key(['History', historyId]);
        const [history] = await datastore.get(historyKey);

        if (!history) {
            return res.status(404).json({ message: 'History tidak ditemukan' });
        }

        res.status(200).json({ history });
    } catch (error) {
        return res.status(500).json({ message: 'Terjadi kesalahan saat mengambil detail history' });
    }
}

module.exports = {
    showName,
    registerUser,
    loginUser,
    logoutUser,
    addNewHistory,
    getAllHistories,
    getHistoryDetail
};
