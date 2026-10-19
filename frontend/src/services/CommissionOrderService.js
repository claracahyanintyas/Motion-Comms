import api from '../api';

const BASE_URL = '/commission-orders';

//need to change so that we dont take user from storage
class CommissionOrderService{
    getAllUserCommission(role) {
        return api.get(`${BASE_URL}`, 
            { role: role || null}, 
            { headers: { Authorization: `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'application/json' } }
        );
    }
  createCommissionOrder = (commissionId) => {
      return api.post(`${BASE_URL}`, 
      { commissionId},
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
          'Content-Type': 'application/json'
        }
      }
    );
  }
  getCommissionOrderDetail = (id) => {
            return api.get(`${BASE_URL}/${id}`, 
            { headers: { Authorization: `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'application/json' } }
        );
  }
    pay(formData, id) {
    return api.post(`${BASE_URL}/${id}/pay`, formData, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
    });
  }

  // payRemainingPayment(formData, id) {
  //   return axios.post(`${REST_API_BASE_URL}/${id}/pay-remaining`, formData, {
  //     headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
  //   });
  // }

  confirmAsBuyer(orderId) {
    const userId = JSON.parse(localStorage.getItem('user')).id; 
    return api.patch(`${BASE_URL}/${orderId}/confirm-by-buyer`, { userId: userId }, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
    });
  }

  confirmAsSeller(orderId) {
    const userId = JSON.parse(localStorage.getItem('user')).id; 
    return api.patch(`${BASE_URL}/${orderId}/confirm-by-seller`, { userId: userId }, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
    });
  }

  getTransactionsByOrder(orderId){
    return api.get(`${BASE_URL}/${orderId}/transactions`, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
    });
  }

  getOrderStatuses = () => {
  return api.get('/commission-orders/statuses');
};


}

export default new CommissionOrderService();

