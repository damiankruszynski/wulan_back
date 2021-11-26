# wulan_back

WuLan back is a project that dynamically generates a directory and file structure list, allowing streaming of movies, video subtitles and photos. The project is a response to the growing costs of the cloud, it allows you to create your own website of this type for free. This project can be your local Netflix platform.

Functions:
  - Ability to download a list of directories, files based on the "path" parmater. In case of empty, paramteter was taken from application.propertis
  - Login with login and password.
  - User registration only, special one-time use code.
  - Create, delete, edit profiles with an account.
  - Movie streaming.
  - Downloading photos from the directory.
  - Download preview photos from the directory.
  - Saving information about the time of a watched movie for a given user profile.
  - Getting time watched movie for a given user profile.
  - Dynamic generation of photo previews.

Security:
By creating an account on the website, you must have a special code, by default, the code is "ABI123" he will be added when service starting up. (data.sql).
When logging in, a token (JWT) will be generated, which will be checked each time per request.
The profileId is embedded in the token in the case of operations modifying the data (with the profileId parameter), identifiers are compared and in case of incompatibility the operation ends with an error.

Base:
H2

Suggested server:
Tomcat
