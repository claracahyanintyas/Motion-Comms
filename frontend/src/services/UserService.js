import api from '../api';

class CommissionService{

    getUser = (id) => api.get(`/users/${id}`);

}

export default new CommissionService();
