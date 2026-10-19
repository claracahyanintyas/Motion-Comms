import api from '../api';

const REST_API_BASE_URL = `/payout-methods`

class PayoutMethodService{

    getActivePayoutMethodsByUser = (id) => api.get(`${REST_API_BASE_URL}/user/${id}`);

    createPayoutMethod = (payoutMethod) => api.post(`${REST_API_BASE_URL}/create`, payoutMethod, {
    headers: {
    Authorization: `Bearer ${localStorage.getItem('token')}`
  }});

  getPayoutMethodTypes = () => api.get(`${REST_API_BASE_URL}/types`)

}

export default new PayoutMethodService();

