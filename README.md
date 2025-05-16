# [2025 APAC Google Solution Challenge] CLOOP-Android

![Frame](https://github.com/user-attachments/assets/30b46775-82fa-4c75-bfc9-c669f8c441a6)
</br></br>

> Wear it, log it, and cycle it  🌱
</br>


CLOOP is a sustainable wardrobe management app that helps you record, organize, and recycle your clothes efficiently.
</br></br>


## Features
<table>
  <tr>
    <td><img width="200" src="https://github.com/user-attachments/assets/24c22fcb-6d70-4fec-9fc8-d0a82dfba932"></td>
    <td><img width="200" src="https://github.com/user-attachments/assets/bdd8bfd9-a0f3-476b-8722-a145def355f4"></td>
    <td><img width="200" src="https://github.com/user-attachments/assets/ac3bee5d-51c1-494a-abde-613c82b2d8b8"></td>
    <td><img width="200" src="https://github.com/user-attachments/assets/52a17bb9-fd33-48a6-95c0-ab046a337584"></td>
    <td><img width="200" src="https://github.com/user-attachments/assets/0194a560-8c3b-40f8-8533-dfa52086df71"></td>
  </tr>
  <tr>
    <td align="center"><b>Splash</b></td>
    <td align="center"><b>Login</b></td>
    <td align="center"><b>Home</b></td>
    <td align="center"><b>Closet</b></td>
    <td align="center"><b>Clothing Wear Stats</b></td>
  </tr>
</table>

</br>

- `Onboarding` : Easy login via Google account integration </br>
- `Home` : Home screen that lets you track your daily outfits via a calendar view </br>
- `Closet` : feature to organize and view registered clothes by category at a glance </br>
- `Clothing Wear Stats` : View comprehensive clothing statistics based on last worn date and total number of wears </br>

</br></br>

<table>
  <tr>
    <td><img width="200" src="https://github.com/user-attachments/assets/14bebd42-148b-4428-b450-e5a0f9ca5938"></td>
    <td><img width="200" src="https://github.com/user-attachments/assets/bdbfe5e6-886b-4e65-b15d-93995e7bc05c"></td>
    <td><img width="200" src="https://github.com/user-attachments/assets/da364562-fe8d-49ff-8c08-89a87cd43d50"></td>
    <td><img width="200" src="https://github.com/user-attachments/assets/47a46fec-c179-49b8-a88e-b81cd6a525f2"></td>
    <td><img width="200" src="https://github.com/user-attachments/assets/05f7f4e0-3017-4d0e-a7bc-00afb7d32e99"></td>
  </tr>
  <tr>
    <td align="center"><b>Add Cloth Dialog</b></td>
    <td align="center"><b>Add Cloth - Manual</b></td>
    <td align="center"><b>Manual 2</b></td>
    <td align="center"><b>Add Cloth - AI</b></td>
    <td align="center"><b>AI 2</b></td>
  </tr>
</table>

</br>

- `Manual Registration` : Allows users to upload a photo and manually enter details such as category, name, brand, color, purchase date, and season </br>
- `AI Registration` : Automatically classifies category, name, color, and season from a single clothing photo using AI. Users simply review the AI-generated results and make minor edits if necessary  </br>

</br></br>


<table>
  <tr>
    <td><img width="200" src="https://github.com/user-attachments/assets/72497d89-1d0c-4764-a627-c58dc0ff09ad"></td>
    <td><img width="200" src="https://github.com/user-attachments/assets/2d298653-153b-4247-bdee-98bce813de5a"></td>
    <td><img width="200" src="https://github.com/user-attachments/assets/925a0e5a-821d-4e37-b2d5-f844051ada56"></td>
    <td><img width="200" src="https://github.com/user-attachments/assets/0135a163-a127-4cf4-a474-7cde1601000d"></td>
  </tr>
  <tr>
    <td align="center"><b>Register Outfit</b></td>
    <td align="center"><b>Select outfit items</b></td>
    <td align="center"><b>Outfit</b></td>
    <td align="center"><b>Registered Outfit</b></td>
  </tr>
</table>

</br>

- `Photo Upload` : Capture and upload a photo to record today’s outfit </br>
- `Item Selection` : Select the clothes worn today from your registered wardrobe </br>
- `Registration Complete` : View the outfit and selected clothing items at a glance </br>

</br></br>

<table>
  <tr>
    <td><img width="200" src="https://github.com/user-attachments/assets/f16aa33c-db12-4b74-b56b-f89158e5cd9c"></td>
    <td><img width="200" src="https://github.com/user-attachments/assets/a5a52f65-e9ef-4ff4-a51b-8fa186e268a5"></td>
    <td><img width="200" src="https://github.com/user-attachments/assets/db8b0e31-bcd4-40ff-b563-6d3d9766a8cb"></td>
    <td><img width="200" src="https://github.com/user-attachments/assets/e20ee2c9-6741-4170-baaf-4193813144f6"></td>
  </tr>
  <tr>
    <td align="center"><b>Donate</b></td>
    <td align="center"><b>Donation Dialog</b></td>
    <td align="center"><b>Donation Completed</b></td>
    <td align="center"><b>Logout</b></td>
  </tr>
</table>

 </br>
 
- `Donation` : Automatically highlights clothing items that have not been worn for over six months </br> 
- `Logout` : Log out via the My Page section </br>

</br></br>


## Tech

### Project Architecture

<img width="740" alt="스크린샷 2025-05-16 오전 1 58 26" src="https://github.com/user-attachments/assets/967565cb-c8e2-4aa2-b462-9d06a45d2a46" />

</br>


### Backend Stack
<table class="tg">
<tbody>
  <tr>
    <td><b>Language</b></td>
    <td>Java 17</td>
  </tr>
<tr>
    <td><b>Framework</b></td>
<td>Spring Boot </td>
</tr>
 <tr>
    <td><b>Database</b></td>
<td>My SQL</td>
</tr>
<tr>
    <td><b>Authentication</b></td>
<td>Google OAuth 2.0, JWT </td>
</tr>
  <tr>
    <td><b>AI Integration</b></td>
<td>Gemini API (via HTTP request) </td>
</tr>
    <tr>
    <td><b>API Docs</b></td>
<td>Swagger (Springdoc OpenAPI) </td>
</tr>
    <tr>
    <td><b>Deployment</b></td>
<td>Docker, AWS EC2 </td>
</tr>
</tbody>
</table>

</br>
