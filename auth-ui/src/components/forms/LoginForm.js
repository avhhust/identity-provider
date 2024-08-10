import React, { useState } from "react";
import InputField from "../ui/InputField.js";
import TextWithLines from "../ui/TextWithLines.js";
import SocialAuthButtons from "../authorization/SocialAuthButtons.js";
import { Link, useNavigate } from "react-router-dom";
import * as authServer from "../../api/apiService.js";

const LoginForm = () => {
    const [userData, setUserData] = useState({
        username: 'test',
        password: 'Test123123',
    });
    const [errors, setErrors] = useState({
        email: [], username: [], password: []
    });
    const [isLoading, setIsLoading] = useState(false);


    const validateField = (field) => {
        const value = userData[field];
        const errorMessages = [];
        if (!value) {
            errorMessages.push("This field is required");
        }
        setErrors(prevErrors => ({ ...prevErrors, [field]: errorMessages }));
        return errorMessages.length === 0;
    }

    const isFormValid = () => {
        let isValid = true;
        for (const field in userData) {
            if (!validateField(field)) isValid = false;
        }
        return isValid;
    }

    const handleInput = (e) => {
        const { name, value } = e.target;
        setUserData(
            prevUserData => ({ ...prevUserData, [name]: value })
        );
    };

    const handleBlur = ({ target }) => {
        validateField(target.name);
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!isFormValid()) return;
        setIsLoading(true);
        const response = await authServer.login(userData);
        if(response.status === 200){
            window.location.href = response.data.redirectUrl;
        }
        setIsLoading(false);
    }

    return (
        <div className="auth_container">
            <h2>Log In</h2>
            <form onSubmit={handleSubmit}>
                <InputField
                    type="text"
                    name="username"
                    value={userData.username}
                    onChange={handleInput}
                    onBlur={handleBlur}
                    placeholder="Username"
                    errors={errors.username}
                    forgotUrl={'/forgot?username'}
                />
                <InputField
                    type="password"
                    name="password"
                    value={userData.password}
                    onChange={handleInput}
                    onBlur={handleBlur}
                    placeholder="Password"
                    errors={errors.password}
                    forgotUrl={'/forgot?password'}
                />
                <Link to={"/forgot?password"} className="forgot_link">Forgot password?</Link>
                <div>
                    <button className="bar auth_btn" type={"submit"} disabled={isLoading}>Log In</button>
                    <p>Don't have an account? <Link to={"/register"}>Sign up</Link></p>
                </div>
            </form>
            <TextWithLines text="or" />
            <SocialAuthButtons />
        </div>
    );
}

export default LoginForm;