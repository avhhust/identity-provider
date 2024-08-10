import React from 'react';
import { Link } from 'react-router-dom';

const NotFoundPage = () => {
    const styles = {
        container: {
            paddingTop: '100px',
            display: 'flex',
            justifyContent: 'center',
            height: '150px'
        },
        button: {
            display: 'block',
            width: 'fit-content',
            marginTop: '10px',
            padding:' 5px 10px',
            borderRadius: '50px',
            border: '1px solid',
            cursor: 'pointer',
            background: 'none',
            color: 'black'
        }
    }
    return (
        <div style={styles.container}>
            <div>
                <h1>404</h1>
                <h2>Oops... Page not found!</h2>
                <p>The page you were looking for, doesn't exist or may have moved</p>
                <Link to={'/login'} style={styles.button}>Go to Login</Link>
            </div>
        </div>
    )
}

export default NotFoundPage;