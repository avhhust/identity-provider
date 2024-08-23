import React from 'react';
import CodeConfirmationForm from '../components/authorization/CodeConfirmationForm';

const CodeConfirmationPage = () => {
  return (
    <CodeConfirmationForm forwardUrl='/reset-password'/>
  )
}

export default CodeConfirmationPage;