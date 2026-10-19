import api from '../api';

class AuthService {
    register(user) {
        return api.post(`/users/register`, user);
    }

    login(credentials) {
        return api.post(`/auth/login`, credentials);
    }

    logout() {
        return api.post(`/auth/logout`);
    }
}

export default new AuthService();