import React from 'react';
import { ValidatorComponent } from 'react-form-validator-core';

class TextValidator extends ValidatorComponent {
  public render() {
    const {
      errorMessages,
      validators,
      requiredError,
      validatorListener,
      ...rest
    } = this.props;

    return (
      <div>
        <input
          className={
            this.state.isValid
              ? 'form-control is-valid'
              : 'form-control is-invalid'
          }
          {...rest}
          ref={(r) => {
            this.input = r;
          }}
        />
        {this.errorText()}
      </div>
    );
  }

  public errorText() {
    const { isValid } = this.state;

    if (isValid) {
      return <div className='valid-feedback'>Success! You've done it.</div>;
    }

    return <div className='invalid-feedback'>{this.getErrorMessage()}</div>;
  }
}

export default TextValidator;
