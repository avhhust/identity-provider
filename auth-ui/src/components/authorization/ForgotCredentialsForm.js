import React, { useState } from 'react';
import InputField from '../ui/InputField';
import NavButton from '../ui/NavButton';
import * as authServer from '../../api/apiService';
import { useNavigate } from 'react-router-dom';

const ForgotCredentialsForm = () => {
  const [inputData, setInputData] = useState('test@test.com');
  const [errors, setErrors] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const isFormValid = () => {
    const errorMessages = [];
    if (!inputData) errorMessages.push("This field is required");
    else if (!inputData.match(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/))
      errorMessages.push("Enter a valid email");
    setErrors(errorMessages);
    return errorMessages.length === 0;
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    if(!isFormValid()) return;
    const response = await authServer.post('/forgot?password', inputData);
    navigate('/otpconfirm');
    setIsLoading(false);
  }

  const handleInput = (e) => {
    const { value } = e.target;
    setInputData(prevInputData => value);
  }

  const handleBlur = (e) => {
    isFormValid();
  }

  return (
    <div className="auth_container">
      <NavButton direction='back' url='/login' label='Login'/>
      <h2>Can't log in?</h2>
      <p style={{fontSize: '14px'}}>Enter the email address associated with your account and we will send you the code to reset your password</p>
      <form onSubmit={handleSubmit}>
        <InputField
          type="text"
          name="Email"
          value={inputData}
          onChange={handleInput}
          onBlur={handleBlur}
          placeholder="Email"
          errors={errors}
        />
        <button className="bar auth_btn" type="submit">Next</button>
      </form>
    </div>
  )
}
export default ForgotCredentialsForm;