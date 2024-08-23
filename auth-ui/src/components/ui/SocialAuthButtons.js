import React from 'react';
import { api } from '../../api/apiService';


const SocialAuthButtons = () => {

    const handleClick = async ({target}) => {
        window.location.href = "http://localhost:9090/api/oauth2/authorization/" + target.id;
    }

    return (
        <>
            <button id="google" className="bar" onClick={handleClick}>Login with Google</button>
            <button id="github" className="bar" onClick={handleClick}>Login with GitHub</button>
        </>
    )
}

export default SocialAuthButtons;