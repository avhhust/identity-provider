import React, {useState} from 'react';
import NavButton from '../ui/NavButton';
import InputField from '../ui/InputField';

const CodeConfirmationForm = (props) => {
    const [inputData, setInputData] = useState('');
    const [errors, setErrors] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
  
    const isFormValid = () => {
      const errorMessages = [];
      if (!inputData) errorMessages.push("This field is required");
      else if (!inputData.match(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/))
        errorMessages.push("Enter a valid email");
      setErrors(errorMessages);
      return errorMessages.length === 0;
    }
  
    const handleSubmit = (e) => {
      e.preventDefault();
      setIsLoading(true);
      if(!isFormValid()) return;
      setIsLoading(false);
    }
  
    const handleInput = (e) => {
      const { value } = e.target;
      setInputData(prevInputData => prevInputData = value);
    }
  
    const handleBlur = (e) => {
      isFormValid();
    }
  
    return (
      <div className="auth_container">
        <NavButton direction='back' url='/forgot?password' label='Back'/>
        <h2>{'Confirmation Code' || props.header}</h2>
        <p style={{fontSize: '14px'}}>{'Enter the confirmation code sent to your email address' || props.text}</p>
        <form onSubmit={handleSubmit}>
          <InputField
            type="text"
            name="code"
            value={inputData}
            onChange={handleInput}
            onBlur={handleBlur}
            placeholder="Code"
            errors={errors}
          />
          <button className="bar auth_btn" type="submit">Confirm</button>
        </form>
      </div>
    )
}

export default CodeConfirmationForm;