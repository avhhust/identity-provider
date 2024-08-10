import React, { useState } from 'react';
import InputField from './InputField';

const OTPVerificationForm = () => {
    const [inputData, setInputData] = useState(''); 
    const [isLoading, setIsLoading] = useState(false);
    const [errors, setErrors] = useState([]);


    const isFormValid = () => {
        if(!inputData) setErrors(["This field is required"]);
        else if(inputData.length !== 8) setErrors(["Code is invalid"]);
    }

    const handleBlur = (e) => {
        
    }

    const handleInput = (e) => {
        const {value} = e.target;
        setInputData(value);
    }

    const handleSubmit = (e) => {
        e.preventDefault();
        setIsLoading(true);
        isFormValid();

        setIsLoading(false);
    }

    const handleResendClick = (e) => {
    }

    return (
        <div className="auth_container">
            <h2>Verify your email address</h2>
            <p className="tiny-text">Enter the verification code sent to your email address</p>
            <form onSubmit={handleSubmit}>
                <InputField
                    type="text"
                    name="OTP"
                    value={inputData}
                    onChange={handleInput}
                    onBlur={handleBlur}
                    placeholder="Verification code"
                    errors={errors}
                />
                <div>
                    <button className="bar" type="submit" id="auth_btn" disabled={isLoading}>Submit</button>
                    <p>Didn't receive code? Send code again</p>
                </div>
            </form>
        </div>
    );
}

export default OTPVerificationForm;