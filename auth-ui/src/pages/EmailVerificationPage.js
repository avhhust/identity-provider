import React from 'react'
import EmailVerificationForm from '../components/authorization/EmailVerificationForm'
import CodeConfirmationForm from '../components/authorization/CodeConfirmationForm';

const EmailVerificationPage = () => {
  return (
    <CodeConfirmationForm header='Verify Email'/>
  )
}

export default EmailVerificationPage;