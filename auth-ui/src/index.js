import './styles/main.css';
import React from 'react';
import ReactDOM from 'react-dom/client';
import reportWebVitals from './reportWebVitals';
import { 
  createBrowserRouter,
  RouterProvider 
} from 'react-router-dom';
import Root from './pages/Root'
import LoginPage from './pages/LoginPage';
import SignupPage from './pages/RegisterPage';
import PrivacyPolicyPage from './pages/PrivacyPolicyPage';
import LegalRoot from './pages/LegalRoot';
import EmailVerificationPage from './pages/EmailVerificationPage';
import NotFoundPage from './pages/NotFoundPage';
import ForgotCredentialsPage from './pages/ForgotCredentialsPage';
import OTPConfirmationPage from './pages/OTPConfirmationPage';

export const router = createBrowserRouter([
  {
    element: <Root/>,
    errorElement: <NotFoundPage />,
    children: [
      {
        path: '/login',
        element: <LoginPage/>,
      },
      {
        path: '/register',
        element: <SignupPage/>,
      },
      {
        path: '/api',
        element: <EmailVerificationPage/>
      },
      {
        path: '/otpconfirm/',
        element: <OTPConfirmationPage/>
      },
      {
        path: '/forgot/',
        element: <ForgotCredentialsPage/>,
        children: [
        ]
      }
    ]
  },
  {
    path: '/legal', 
    element: <LegalRoot/>,
    children: [
      {
        path: 'privacy',
        element: <PrivacyPolicyPage/>
      },
      {
        path: 'terms',
        element: ''
      }
    ]
  }
]);

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <RouterProvider router={router}/>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();