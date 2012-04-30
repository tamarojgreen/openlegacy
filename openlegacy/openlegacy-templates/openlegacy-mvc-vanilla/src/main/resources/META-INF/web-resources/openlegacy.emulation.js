var TerminalContext = new function() {
	this.canvas = null;
	this.currentSnapshot = null;
	this.viewer = null;
};

var TerminalUtils = new function() {
	this.getFieldByPosition = function(snapshot, position) {
		if (snapshot == null) {
			return null;
		}
		var filteredFields = dojo.filter(snapshot.fields, function(field) {
			if (field.position.row == position.row
					&& position.column >= field.position.column
					&& position.column <= field.endPosition.column) {
				return true;
			}
			return false;
		});
		if (filteredFields.length == 0) {
			return null;
		}
		return filteredFields[0];
	};

	this.draw = function(canvas, snapshot) {
		TerminalUtils.clearArea(canvas, snapshot.size.columns,
				snapshot.size.rows);
		TerminalUtils.drawCursor(canvas, snapshot);
		dojo.forEach(snapshot.fields, function(field, i) {
			console.debug(field.value + ":" + field.position.row + ","
					+ field.position.column);
			TerminalUtils.drawField(canvas, field, false);
		});
	};

	this.drawField = function(canvas, field, clear) {
		if (clear) {
			this.clearField(canvas, field);
		}
		var x = this.toWidth(field.position.column);
		var y = this.toHeight(field.position.row);
		var x2 = this.toWidth(field.endPosition.column);
		canvas.fillText(field.value, x, y);

		if (field.editable) {
			canvas.moveTo(x, y);
			canvas.lineTo(x2, y);
			canvas.stroke();
		}
	};

	this.drawCursor = function(canvas, snapshot) {
		var cursorPosition = snapshot.cursorPosition;
		this.drawRectangle(canvas, this.toWidth(cursorPosition.column), this
				.toHeight(cursorPosition.row - 1), this.toWidth(1), this
				.toHeight(1), true);
	};

	this.clearCursor = function(canvas, snapshot) {
		var cursorPosition = snapshot.cursorPosition;
		this.clearRowArea(canvas, cursorPosition, 1);
	};

	this.clearField = function(canvas, field) {
		this.clearRowArea(canvas, field.position, field.endPosition.column
				- field.position.column + 1);
	};

	this.clearRowArea = function(canvas, startPosition, length) {
		canvas.clearRect(this.toWidth(startPosition.column) - 1, this
				.toHeight(startPosition.row - 1) - 1, this.toWidth(length) + 2,
				this.toHeight(1) + 2);
	};
	this.clearArea = function(canvas, columnsCount, rowsCount) {
		canvas.clearRect(1, 1, this.toWidth(columnsCount + 1) + 6, this
				.toHeight(rowsCount + 1) + 2);
	};

	// helper function
	this.drawRectangle = function(canvas, x, y, w, h, fill) {
		canvas.beginPath();
		canvas.rect(x, y, w, h);
		canvas.closePath();
		canvas.stroke();
		if (fill) {
			canvas.fill();
		}
	};

	this.toWidth = function(column) {
		return parseInt(column) * 10;
	};

	this.toHeight = function(row) {
		return parseInt(row) * 20;
	};

};

function TerminalSession() {

	TerminalContext.canvas = terminalViewer.getContext('2d');
	TerminalContext.canvas.clearRect(0, 0, terminalViewer.width,
			terminalViewer.height);
	TerminalContext.canvas.fillStyle = "rgb(0,0,0)";
	TerminalContext.canvas.font = '19px Courier New';

	dojo.connect(document, "onkeydown", function(event) {
		TerminalUtils.clearCursor(TerminalContext.canvas,
				TerminalContext.currentSnapshot);
		switch (event.keyCode) {

		case 37:
			TerminalContext.currentSnapshot.cursorPosition.column--;
			break;
		case 38:
			TerminalContext.currentSnapshot.cursorPosition.row--;
			break;
		case 39:
			TerminalContext.currentSnapshot.cursorPosition.column++;
			break;
		case 40:
			TerminalContext.currentSnapshot.cursorPosition.row++;
			break;
		default:
			break;
		}
		TerminalUtils.drawCursor(TerminalContext.canvas,
				TerminalContext.currentSnapshot);
	});
	dojo.connect(document, "onkeypress", function(event) {
		var field = TerminalUtils.getFieldByPosition(
				TerminalContext.currentSnapshot,
				TerminalContext.currentSnapshot.cursorPosition);
		if (field == null) {
			return;
		}
		field.value += String.fromCharCode(event.charCode);
		TerminalUtils.drawField(TerminalContext.canvas, field, true);
		TerminalContext.currentSnapshot.cursorPosition.column++;
		TerminalUtils.drawCursor(TerminalContext.canvas,
				TerminalContext.currentSnapshot);
	});

	this.fetchSnapshot = function() {
		dojo.xhrGet({
			url : "Emulation.json",
			handleAs : "json",
			load : function(snapshot) {
				TerminalContext.currentSnapshot = snapshot;
				TerminalUtils.draw(TerminalContext.canvas, snapshot);
			}
		});
	};

	this.doAction = function(command) {
		TerminalContext.currentSnapshot.command = command;
		var xhrArgs = {
			url : "Emulation.json",
			postData : dojo.toJson(TerminalContext.currentSnapshot),
			handleAs : "json",
			headers : {
				"Content-Type" : "application/json"
			},
			load : function(snapshot) {
				TerminalContext.currentSnapshot = snapshot;
				TerminalUtils.draw(TerminalContext.canvas, snapshot);
			},
			error : function(error) {
				alert(error);
			}
		};

		dojo.xhrPost(xhrArgs);
	};

}

var terminalSession;

dojo.addOnLoad(function() {
	TerminalContext.viewer = dojo.byId('terminalViewer');
	terminalSession = new TerminalSession();
	terminalSession.fetchSnapshot();
});
