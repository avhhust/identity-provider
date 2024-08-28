import React, { useState } from "react";
import InputField from "../ui/InputField.js";
import TextWithLines from "../ui/TextWithLines.js";
import SocialAuthButtons from "../ui/SocialAuthButtons.js";
import { Link } from "react-router-dom";
import * as authServer from "../../api/apiService.js";

const LoginForm = () => {
    const [userData, setUserData] = useState({
        username: '', password: '',
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
            if (errors[field].length !== 0) {
                isValid = false;
            }
            else if (!validateField(field)) {
                isValid = false;
            }
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
        if (response.status === 200) {
            window.location.href = response.data.redirectUrl;
        }
        else if(response.status === 401){
            for(let field in errors){
                setErrors(prevErrors => ({...prevErrors, [field]: ["Username or password is incorrect!"]}));
            }
        }
        // Handle other codes
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
                <Link to={"/forgot?password"} className="text_under_input right">Forgot password?</Link>
                <div>
                    <button className="bar submit_btn" type={"submit"} disabled={isLoading}>Log In</button>
                    <p>Don't have an account? <Link to={"/register"}>Sign up</Link></p>
                </div>
            </form>
            <TextWithLines text="or" />
            <SocialAuthButtons />
        </div>
    );
}

export default LoginForm;