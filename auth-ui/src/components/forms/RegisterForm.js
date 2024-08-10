import React, { useState } from "react";
import axios from "axios";
import InputField from "../ui/InputField.js";
import TextWithLines from "../ui/TextWithLines.js"
import SocialAuthButtons from "../authorization/SocialAuthButtons.js";
import { Link, useNavigate } from "react-router-dom";
import * as authServer from "../../api/apiService.js";

const initialState = { email: [], username: [], password: [] }

const SignupForm = () => {
    const [userData, setUserData] = useState({  // stores user input data from the form
        email: "email@test.com", username: "email", password: "Email123123"
    });
    const [errors, setErrors] = useState(initialState); // used to store error messages that are displayed under the input field
    const [isLoading, setIsLoading] = useState(false); // used to prevent submissions while http request is processing
    const navigate = useNavigate();

    const validateField = (field) => {
        // preforms validation for only one field and sets error message for that field to be displayed
        const value = userData[field];
        const errorMessages = [];
        if (!value) {
            errorMessages.push("This field is required");
        }
        else {
            if (field === "email") {
                if (!value.match(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/)) {
                    errorMessages.push("Enter a valid email")
                }
            } else if (field === "username") {
                if (value.length < 3 || value.length > 15) {
                    errorMessages.push("Should be between 3 and 15 characters long");
                }
                if (!value.match(/^(?=.*[a-zA-Z])(?!^\d+$)[a-zA-Z\d]+$/)) {
                    errorMessages.push("It can only contain letters and numbers");
                }
            } else if (field === "password") {
                if (value.length < 8) {
                    errorMessages.push("Shoud contain at least 8 symbols");
                }
                if (!value.match(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).+/)) {
                    errorMessages.push("Should contain at least 1 capital letter and 1 number ")
                }
            }
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

    // Calls validation function to validate field when user unfocuses input
    const handleBlur = ({ target }) => {
        // setHints(initialState); // hide hints
        validateField(target.name);
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!isFormValid()) return;
        setIsLoading(true);

        const response = await authServer.register(userData);
        if(response.status === 201){
            navigate("/login");
        }
         else if(response.details){
            const {details} = response;
            for(const field in details){
                setErrors(prevErrors => ({...prevErrors, [field]: [details[field]]}));
            }
        }
        setIsLoading(false);
        // } catch (error){
        //     if(error.response){
        //         if(error.response.data.details){
        //             const details = error.response.data.details;
        //             // iterate over exception details which contains
        //             for(const field in details){
        //                 setErrors(prevErrors => ({...prevErrors, [field]: [details[field]]}));
        //             }
        //         }
        //     }
        // }
    };

    return (
        <div className="auth_container">
            <h2>Sign Up</h2>
            <form onSubmit={handleSubmit}>
                <InputField
                    type="text"
                    name="email"
                    value={userData.email}
                    onChange={handleInput}
                    onBlur={handleBlur}
                    placeholder="Email"
                    errors={errors.email}
                />
                <InputField
                    type="text"
                    name="username"
                    value={userData.username}
                    onChange={handleInput}
                    onBlur={handleBlur}
                    placeholder="Username"
                    errors={errors.username}
                />
                <InputField
                    type="password"
                    name="password"
                    value={userData.password}
                    onChange={handleInput}
                    onBlur={handleBlur}
                    placeholder="Password"
                    errors={errors.password}
                />
                <div>
                    <button className="bar auth_btn" type="submit" disabled={isLoading}>Sign Up</button>
                    <p>Already have an account? <Link to={"/login"}>Login</Link></p>
                </div>
            </form>
            <TextWithLines text="or" />
            <SocialAuthButtons />
            <div className="agreement">
                <p>By signing up, you agree to our <Link to={"/legal/terms"}>Terms of Service</Link> and <Link to={"/legal/privacy"}>Privacy Policy</Link></p>
            </div>
        </div>
    );
}

export default SignupForm;