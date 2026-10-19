import api from '../api';

const REST_API_BASE_URL = `/commissions`

class CommissionService{

    getAllCommissions = () => api.get(`${REST_API_BASE_URL}`);

    getCommission = (id) => api.get(`${REST_API_BASE_URL}/${id}`);

    createCommission = (commission) => api.post(`${REST_API_BASE_URL}`, commission, {
    headers: {
    Authorization: `Bearer ${localStorage.getItem('token')}`
  }});

    updateCommission = (id, commission) => api.put(`${REST_API_BASE_URL}/${id}`, commission, {
    headers: {
    Authorization: `Bearer ${localStorage.getItem('token')}`
  }});

}

export default new CommissionService();

