## Simen's Trade Simulator

Simen's Trade Simulator is a simple virtual trading platform for U.S. market stocks, ETFs, and options.

You can check the real-time market data for common stocks, ETFs, and options, even OTC and ADR symbols.

This project has already been deployed to AWS.

visit http://virtualtradeground.com/login

you can open an account [here](http://virtualtradeground.com/signup).

----
### Notes
* you can choose a Cash or a Margin account. Margin account has leveraged buying power, while a cash account's buying power is limited to its balacne
* your account balance, total market value, and net equity is calculated real-time, below your positions.
* if you want to search an option symbol, your input must follow the OCC options symbol format, without any spaces in the middle


### [The OCC Option Symbol](https://en.wikipedia.org/wiki/Option_symbol)

The OCC option symbol consists of four parts:

1. Root symbol of the underlying stock or ETF
2. Expiration date, 6 digits in the format yymmdd
3. Option type, either P or C, for put or call
4. Strike price, as the price x 1000, front padded with 0s to 8 digits

Examples:

`SPX   141122P00019500`

The above symbol represents a put on SPX, expiring on 11/22/2014, with a strike price of $19.50.

`LAMR  150117C00052500`

The above symbol represents a call on LAMR, expiring on 1/17/2015, with a strike price of $52.50.