import './App.css'
import HeaderComponent from './components/HeaderComponent'
import AllCommissions from './pages/AllCommissions'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import HomePage from './pages/HomePage'
import CommissionDetailsPage from './pages/CommissionDetailsPage'
import UserDetailsPage from './pages/UserDetailsPage'
import { UploadCommission } from './pages/UploadCommission'
import RegisterComponent from './components/RegisterComponent'
import LoginComponent from './components/LoginComponent'
import LogoutComponent from './components/LogoutComponent'
import AllCommissionOrdersPage from './pages/AllCommissionOrdersPage'
import CommissionOrderDetails from './components/CommissionOrderDetails'
import CreatePayoutMethodPage from './pages/CreatePayoutMethodPage.jsx'
import TransactionPage from './pages/TransactionPage'
import { Toaster } from 'react-hot-toast';

function App() {

  return (
    <>
      <Toaster
        position="top-center"
        toastOptions={{
          duration: 4000,
          className: 'bg-[#567C8D] text-white rounded-md px-4 py-2 shadow-lg'
        }}
      />
      <BrowserRouter>
        <HeaderComponent />
        <Routes>
          <Route path='/' element = { <HomePage />}></Route>
          <Route path='/commissions' element = { <AllCommissions />}></Route>
          <Route path='/commissions/:id' element = {<CommissionDetailsPage />}></Route>
          <Route path='/upload-commission' element = {<UploadCommission />}></Route>
          <Route path='/users/:id' element = {<UserDetailsPage />}></Route>
          <Route path="/register" element={<RegisterComponent />} />
          <Route path="/login" element={<LoginComponent />} />
          <Route path="/logout" element={<LogoutComponent/>}/>
          <Route path="/commission-orders" element={<AllCommissionOrdersPage/>} />
          <Route path="/commission-orders/:id" element={<CommissionOrderDetails/>} />
          <Route path="/payout-methods/create" element={<CreatePayoutMethodPage/>} />
          <Route path="/transaction/:id" element={<TransactionPage />} />
        </Routes>

      </BrowserRouter>
    </>
  )
}

export default App
