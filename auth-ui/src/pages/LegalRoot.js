import React from 'react'
import { Outlet, Link } from 'react-router-dom';

const LegalRoot = () => {
  return (
    <div className='legal_cont'>
      <ol className="menu">
        <li>
          <Link to="/legal/privacy">Privacy Policy</Link>
        </li>
        <li>
          <Link to="/legal/terms">Terms of Service</Link>
        </li>
      </ol>
      <div className="wrapper">
        <Outlet/>
      </div>
    </div>
  )
}

export default LegalRoot;
