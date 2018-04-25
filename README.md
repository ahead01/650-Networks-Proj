# 650-Networks-Proj
Github for Group Project

Run the server and then the client. Client will prompt for user input

Client automatically sends a STOP packet to server after its finished sending the entire message it gets from the website. The STOP packet tells the server to shutdown. This default behavior can be turned off by commenting out the myClient.stopConnection(); line in the main routiene of the client. If you do this the you will either need to rerun the client with myClient.stopConnection(); enabled or manually kill the process that is listening on port 12321. Run $ lsof -i | grep 12321 - on osx and something simillar on unix. I think its ipconfig | findstr 12321 on windows. SOmethign like that. Then you need to get the process id of the process listening on 12321 and kill it manualy.

Server has a default delay of 65ms for testing

Uncoment the testing portion of the client code and the client will send a TEST packet instead of retransmitting the last sent packet. This will change the server's delay to 10 ms. Allowing the first packet to time out and the second to succeed if you set the client timeout to something between 10 and 65 ms.

Not 100% sure about the threading of the server printing out the recieved packets

