var DEBUG = false;
var driverIndex = 0, operatorIndex = 1;
var controllersNum = 0;
var cameraSetting = 0;
var prevDriveButton;
var prevOpButton;
$(document).ready(function(){start()});
document.onkeydown = checkKey;

function readDriver(){
  var gp = navigator.getGamepads()[driverIndex];
  if (gp == null){
    console.log("Driver's controller is null.");
  }
  else if (!gp.connected) {
    console.log("Driver's controller is not connected");
  }
  return gp;
}

function readOperator(){
  var gp = navigator.getGamepads()[operatorIndex];
  if (gp == null){
    console.log("Operator's controller is null.");
  }
  else if (!gp.connected) {
    console.log("Operator's controller is not connected");
  }
  return gp;
}

function checkKey(e) {
  e = e || window.event;
  cameraSetting = (cameraSetting+1)%2;
}

function start(){
  for (var i = 0; i < navigator.getGamepads().length; i++){
    if (navigator.getGamepads()[i] != undefined) {
      if (navigator.getGamepads()[i].connected)
        addController(i);
    }
  }

  setInterval(main,50);
}

window.addEventListener("gamepadconnected",function(e) {
  addController(e.gamepad.index);
});

window.addEventListener("gamepaddisconnected", function(e) {
  removeController(e.gamepad.index);
});

function addController(controllerNum) {
  $("#controller").append("<p id='controller" + controllerNum + "' >" + controllerNum + "</p>");
  $("#driver").append("<li id='driver" + controllerNum + "'><input type='checkbox' name='driver" + controllerNum + "' value=''></input></li>");
  $("#operator").append("<li id='operator" + controllerNum + "'><input type='checkbox' id='operator' name='operator" + controllerNum + "' value=''></input></li>");
  $("#pressed").append("<li id='pressed" + controllerNum + "'><button type='button' class='btn btn-default' style='background-color:red' name='pressed" + controllerNum + "' ></button>");
  if (controllersNum == 0) {
	  $("#driver0").find("input").prop("checked", true);
	  driverIndex = 0;
  }
  else if (controllersNum == 1) {
	  $("#operator1").find("input").prop("checked", true);
	  operatorIndex = 1;
  }
  $("#driver" + controllerNum).find("input").click(function() {
	if ($("#driver" + controllerNum).find("input").is(":checked")) {
		if ($("#operator" + controllerNum).find("input").is(":checked")) {
			$("#operator" + controllerNum).find("input").prop("checked", false);
			operatorIndex = -1;
		}
		for (var i = 0; i < controllersNum; i++) {
			if (i != controllerNum) {
				$("#driver" + i).find("input").prop("checked", false);
			}
		}
		driverIndex = controllerNum;
		console.log("Driver Index:" + driverIndex);
		console.log("Operator Index:" + operatorIndex);
	}
  });
    $("#operator" + controllerNum).find("input").click(function() {
	if ($("#operator" + controllerNum).find("input").is(":checked")) {
		if ($("#driver" + controllerNum).find("input").is(":checked")) {
			$("#driver" + controllerNum).find("input").prop("checked", false);
			driverIndex = -1;
		}
		for (var i = 0; i < controllersNum; i++) {
			if (i != controllerNum) {
				$("#operator" + i).find("input").prop("checked", false);
			}
		}
		operatorIndex = controllerNum;
		console.log("Operator Index:" + operatorIndex);
		console.log("Driver Index:" + driverIndex);
	}
  });
  controllersNum++;
}
function removeController(controllerNum) {
  $("#controller" + controllerNum).remove();
  $("#driver" + controllerNum).remove();
  $("#operator" + controllerNum).remove();
  $("#pressed" + controllerNum).remove();
  controllersNum--;
}

function main(){
  readDriver();
  readOperator();
  updatePressed();
  cameraControl();
  selectFeed();
}

function updatePressed() {
  for (var i = 0; i < navigator.getGamepads().length; i++) {
	var gp = navigator.getGamepads()[i];
	if (gp != null) if (gp != undefined) {
		if (getControllerActive(gp.index) ) {
			$("#pressed" + gp.index).find("button").css("background-color", "green");
		}
		else {
			$("#pressed" + gp.index).find("button").css("background-color", "red");
		}
	}
  }
}

function getControllerActive(index) {
	var gp = navigator.getGamepads()[index];

  if (gp != null) if (gp != undefined) {
	for (var i = 0; i < gp.axes.length; i++) {
	  if (gp.axes[i] > 0.25 || gp.axes[i] < -0.25) {
	    return true;
	  }
	}
	for (var i = 0; i < gp.buttons.length; i++) {
	  if (gp.buttons[i].value) {
	    return true;
	  }
    }
  }
  return false;
}

function cameraControl() {
	var changeSetting = false;
	var driverButton;
	var opButton;

	if (driverIndex >= 0) if (navigator.getGamepads()[driverIndex] != null) if (navigator.getGamepads()[driverIndex] != undefined) {
		driverButton = navigator.getGamepads()[driverIndex].buttons[5].pressed;
		if (driverButton && !prevDriverButton) {
			changeSetting = true;
		}
	}

	if (operatorIndex >= 0) if (navigator.getGamepads()[operatorIndex] != null) if (navigator.getGamepads()[operatorIndex] != undefined) {
		opButton = navigator.getGamepads()[operatorIndex].buttons[4].pressed;
		if (opButton && !prevOpButton) {
			changeSetting = true;
		}
	}
		
	if (changeSetting) {
		cameraSetting = (cameraSetting+1) % 2;
	}
	
	prevDriverButton = driverButton;
	prevOpButton = opButton;
}

if (!DEBUG) {
	var FEED_DEFAULT = "default.png",
		FEED_GEAR = "http://10.16.40.183/mjpeg.cgi?user=admin&password=&channel=0",
		FEED_SHOOTING = "http://10.16.40.184/video1.mjpg",
		FEED_CLIMBING = "climbing.png";
		//Axis example url: http://10.16.40.181/mjpg/video.mjpg
}
else {
	var FEED_DEFAULT = "default.png",
		FEED_GEAR = "driving.png",
		FEED_SHOOTING = "default.png";
		FEED_CLIMBING = "climbing.png";
}
function selectFeed() {
	if (cameraSetting == 0) {
		feed = FEED_GEAR
	}
	else if (cameraSetting == 1) {
		feed = FEED_SHOOTING;
	}
	else if (cameraSetting == 2) {
		feed = FEED_CLIMBING;
	}
	else {
		feed = FEED_DEFAULT;
	}

	$(".cameraImage").prop("src", feed);
	$("#crosshairCenter").prop("height", $(".cameraImage").height());
}
