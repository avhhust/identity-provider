import React from 'react';


const SocialAuthButtons = () => {

    const handleClick = ({target}) => {
        // window.location.href = BASE_URL + "/oauth2/authorization/" + target.id;
    }

    return (
        <>
            <button id="google" className="bar" onClick={handleClick}>Login with Google</button>
            <button id="github" className="bar" onClick={handleClick}>Login with GitHub</button>
        </>
    )
}

export default SocialAuthButtons;