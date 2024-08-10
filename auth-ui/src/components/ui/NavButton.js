import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';

const NavButton = (props) => {
  const {direction, label, url, disabled} = props;

  const style = {
    position: 'absolute',
    top: '14px',
    left: direction === 'back' ? '14px' : 'auto',
    right: direction === 'back' ? 'auto' : '12px',
    padding: '5px',
    fontWeight: 'bold',
    fontSize: '15px',
    cursor: 'pointer',
  }

  const symbol = direction === 'back' ? '< ' + (label || 'Go back') : 
                                        (label || 'Go forward') + ' >';

  return (
    <div style={style}>
      <Link to={url}>{symbol}</Link>
    </div>
  )
}

export default NavButton;