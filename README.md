# photoprism-setup
Docker scripts and web client for accessing photoprism. This is using traefik as the reverse proxy and ofilea for scheduling import and convert jobs on photoprism. An android app was
created to handle mTLS since no mobile browersers seem to support it.

# mTLS
Mutual TLS is used as an extra layer of security. The reverse proxy ensures that the client presents a valid certificate before proceeding with the connection.
Generate a root certifcate and a client certificate. The client certificate gets stored in a path that traefik can see. Android expects the cert to be in pksc12 format, so the
cert that gets generated must be converted. For the `in` argument to openssl, the cert needs to contain both the ca cert and the client cert cat'd together. The pksc12 file can 
then be stored in `res/raw/bundle.p12` and the app should work as is.

# Disclaimer
I'm not an Android developer. I have combined examples from the web to get something working. Please submit a PR if there are better ways to do things.

# References
* https://stackoverflow.com/questions/35135225/android-webview-handle-onreceivedclientcertrequest
* https://blog.devolutions.net/2020/07/tutorial-how-to-generate-secure-self-signed-server-and-client-certificates-with-openssl/#step-34-generate-client-certificate-pfx
