# cryptocurrency-exchange

### How to install?
1. Download repo
2. Make sure you have installed JDK, otherwise install it
3. Go to the project folder, open terminal and run following command
   - for Windows
   ```
   .\mvnw install
   ```
   - for Linux
   ```
   chmod u+x mvnw
   ./mvnw install
   ```
4. Go to the target folder, and run following command
   ```
   java -jar trading-platform-0.0.1-SNAPSHOT.jar
   ```

<hr>

The application simulates the cryptocurrency exchange. The user can create his account to build his portfolio. An inexperienced user can start his adventure with cryptocurrencies without losing his own money, and an experienced trader can compete with other users to be at the top of the ranking.
   
#### home page
![home page demo](demo/home.png)
<hr>

#### registration page
![registration page demo](demo/sign_up.png)
<hr>

#### verification page
> After succesful registration, the user receives an e-mail to activate the account. The generated token is valid for 24 hours.

![verification page demo](demo/verification.png)
![activated account page demo](demo/activated.png)
<hr>

#### login page
![login page demo](demo/sign_in.png)
<hr>

#### wallet page
> The user can check the status of his portfolio - owned funds and cryptocurrencies.

![wallet page demo](demo/wallet.png)
<hr>

#### trade page
> The user can check the available cryptocurrencies and their prices, and then choose the one he wants to trade.

![trade page demo](demo/trade.png)
<hr>

#### trade cryptocurrency page
> The user enters the amount of the cryptocurrency he wants to sell or buy. If the given amounts are incorrect - the user does not have enough funds or cryptocurrencies, the button becomes disabled. Below the button, the profit or cost of the transaction is displayed, calculated on the basis of the entered amount values.

![trade cryptocurrency page demo](demo/buying.png)
![disabled trade cryptocurrency page demo](demo/disabled_buying.png)
<hr>

#### ranking page
> Ranking of all active users.

![ranking page demo](demo/ranking.png)
<hr>

#### history page
> The user can check the history of all his transactions.

![history page demo](demo/history.png)
<hr>

#### settings page
> The user can change his password and e-mail address.

![settings page demo](demo/settings.png)
<hr>
