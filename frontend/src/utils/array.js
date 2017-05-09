Array.prototype.clear = function() {

	while (this.length > 0) {
		this.pop();
	}
};

Array.prototype.pushArray = function(arr) {
	this.push.apply(this, arr);
};