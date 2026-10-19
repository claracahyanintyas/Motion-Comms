import toast from 'react-hot-toast';
import AuthService from '../services/AuthService';
import { useNavigate, Link } from 'react-router-dom';

function LogoutComponent() {

        const navigate = useNavigate();
    
        const handleLogout = async () => {
            try {
                await AuthService.logout();
                toast.success("Successfully logged out")
                navigate('/');
            } catch (error) {
                const message = error.response.data.message || error.message || "Something went wrong"
                toast.error(message);
            }
        };

  return (
    <button className='bg-[#567C8D] p-2 text-white rounded-lg' onClick={handleLogout}>
        Logout
    </button>
  )
}

export default LogoutComponent