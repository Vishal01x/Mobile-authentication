This application is designed for mobile number authentication using OTP (One-Time Password) verification via Firebase Authentication. It incorporates a country code picker library for selecting the country, which is integrated with an input EditText to format the mobile number according to the selected country code, ensuring it is ready for verification if the number is valid. Mobile number validation is performed using the libphonenumber library from Google.

Key Features
Country Code Picker: The app utilizes a country code picker library, enabling users to select their country, automatically formatting their mobile number accordingly.
Mobile Number Validation: Validation is handled using the Google libphonenumber library to ensure the mobile number conforms to the standard format of the selected country.
OTP Verification: Once the mobile number is validated, the OTP screen is presented. Users must enter the exact OTP sent via Firebase to complete the verification process.
Data Retention: If users need to change their mobile number, they can navigate back to the mobile number screen by clicking on the displayed number. The selected country and corresponding mobile number are retrieved from the ViewModel, ensuring data retention across different states and configuration changes.
Additional User Information: After mobile number verification, users are prompted to provide additional details such as their name and Gmail for further verification, completing the user authentication process.
This comprehensive mobile number authentication flow ensures a smooth and secure verification process, leveraging advanced libraries and Firebase services for optimal performance and reliability.
