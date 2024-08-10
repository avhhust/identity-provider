import React, { useState } from 'react';
import InputField from '../ui/InputField';

const EmailVerificationForm = () => {
    const [inputData, setInputData] = useState(''); 
    const [isLoading, setIsLoading] = useState(false);
    const [errors, setErrors] = useState([]);

    const isFormValid = () => {
        const errorMessages = [];
        if(!inputData) errorMessages.push("This field is required");
        else if(inputData.length !== 8) errorMessages.push("Code is invalid");
        setErrors(errorMessages);
        return errorMessages.length === 0;
    }

    const handleBlur = () => {
        isFormValid();
    }

    const handleInput = (e) => {
        const {value} = e.target;
        setInputData(value);
    }

    const handleSubmit = (e) => {
        e.preventDefault();
        setIsLoading(true);
        if(!isFormValid()) return;

        setIsLoading(false);
    }

    const handleResendClick = (e) => {
        
    }

    return (
        <div className="auth_container sub_container">
            <h2>Verify your email address</h2>
            <p>Enter the verification code sent to your email address</p>
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
                <button className="bar auth_btn" type="submit" disabled={isLoading}>Submit</button>
                <div>
                    <p>Didn't receive code?</p>
                    {/* <a>Send code again</a> */}
                </div>
            </form>
        </div>
    );
}

export default EmailVerificationForm;