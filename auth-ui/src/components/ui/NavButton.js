import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';

const NavButton = (props) => {
  const { direction, label, url, replace } = props;

  const style = {
    position: 'absolute',
    top: '14px',
    left: direction === 'back' ? '16px' : 'auto',
    right: direction === 'back' ? 'auto' : '14px',
    padding: '5px',
    fontWeight: 'bold',
    fontSize: '15px',
    cursor: 'pointer',
    color: '#1d3e5bec'
  }

  const symbol = direction === 'back' ? '< ' + (label || 'Go back') : (label || 'Go forward') + ' >';
  const navInHistory = direction === 'back' ? -1 : +1;

  return (
    <Link
      style={style}
      to={url || navInHistory}
      replace={replace || false}
    >
      {symbol}
    </Link>
  )
}

export default NavButton;