import api from "../api";

const REST_API_BASE_URL = `/transactions`

class TransactionService {
  confirmTransaction = (id) =>
    api.patch(`${REST_API_BASE_URL}/${id}/confirm`, {
    headers: {
    Authorization: `Bearer ${localStorage.getItem('token')}`
  }});
}

export default new TransactionService();
