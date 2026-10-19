import React, { useContext } from 'react';
import { AuthContext } from '../context/AuthContext';
import { Link } from 'react-router-dom';
import logo from '../assets/logo.png';

const HeaderComponent = () => {
  const { isAuthenticated, logout, user } = useContext(AuthContext);
  return (
    <div className="z-50 ">
        <header className="">
            <nav className="bg-[#C8D9E6] shadow-md flex sticky w-full top-0 h-20 p-2 items-center justify-between">
              <div className='flex items-center gap-4'>
                <Link to="/">
                  <img
                    src={logo}
                    alt="Site Logo"
                    className="h-18 w-auto nav-link"
                  />
                </Link>
                <Link className="hover:text-[#567C8D]" to='/'>Home</Link>
                <Link className="hover:text-[#567C8D]" to="/commissions">Commissions</Link>
                <Link className="hover:text-[#567C8D]" to="/upload-commission">Upload Commission</Link>
                <Link className="hover:text-[#567C8D]" to="/commission-orders">My orders</Link>
              </div>
              <div>
                {isAuthenticated ? (
                    <>
                        <a className="nav-link hover:text-[#567C8D] p-2" href={`/users/${user.id}`}>Profile</a>
                        <button onClick={logout}>Logout</button>
                    </>
                ) : (
                    <>
                        <Link to="/login">Login</Link>
                    </>
                )}
              </div>
            </nav>
        </header>
    </div>
  )
}

export default HeaderComponent;