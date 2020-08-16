
function updateLastPriceAndMarketValue(url, multiplier, priceElement, marketValueElement, quantity) {
  var xhttp;
  xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {

      //updates price field
      priceElement.innerHTML = this.responseText;
      //updates market value field, round to 2 decimal places
      marketValueElement.innerHTML = (parseFloat(this.responseText) * quantity * multiplier).toFixed(2);
    }
  };
  xhttp.open("GET", url, true);
  xhttp.send();
}

//loop through all .last-price class elements to display price for all positions
function updateAllPositions() {
  var quantityFields = document.getElementsByClassName(`quantity`);
  var priceFields = document.getElementsByClassName(`last-price`);
  var symbolFields = document.getElementsByClassName(`symbol`);
  var MVFields = document.getElementsByClassName(`market-value`);
  if (priceFields.length !== symbolFields.length){
    alert(`the amount of symbols and price fields are not equal`);
    return null;
  }
  var i = 1;
  var totalMV = 0;
  var multiplier = 1;
  //for options
  for( i; i<priceFields.length; i++){
    //calls server side /quote service using the symbol from innerText. Once get a 200 response, update the price field and market value
    var symbol = symbolFields[i].innerText;
    if (symbol.length>5) multiplier = 100;
    updateLastPriceAndMarketValue(`/quote?symbol=${symbol}`, multiplier, priceFields[i], MVFields[i], parseInt(quantityFields[i].innerHTML));
    totalMV += parseFloat(MVFields[i].innerHTML);
  }
  document.getElementById("total-mv").innerHTML = totalMV.toFixed(2);
  var balance = parseFloat(document.getElementById("balance").innerHTML);
  document.getElementById("net-equity").innerHTML = (totalMV + balance).toFixed(2);
}

//continuously update stock price. run updateAllPositions every 3 seconds
setInterval(updateAllPositions, 3000);
window.onload = updateAllPositions;