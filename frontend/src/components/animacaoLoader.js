var AnimacaoLoader = function() {};
AnimacaoLoader.prototype.openPreloader = function(load){

	if(!load.preloader) {
		load.preloader = new this.GSPreloader();
	}

	//open the preloader
	load.preloader.active(true);
	var elemPreloader = load.elemento;
	$(elemPreloader).block({message: null});
	return load;
};

AnimacaoLoader.prototype.closePreloader = function(load){

	$(load.elemento).unblock();
	load.preloader.active(false);
};

var configLoad = {
	radius:20,
	dotSize:7,
	dotCount:8,
	colors:['#61AC27','yellow','purple','#FF6600'], //have as many or as few colors as you want.
	boxOpacity:0.2,
	boxBorder:'1px solid #AAA',
	animationOffset: 1.8,
};

AnimacaoLoader.prototype.GSPreloader = function(config) {

	var options = configLoad;
	options.parent = config ? config.parent : undefined;

	var parent = options.parent || document.body;
	var element = this.element = $('<div />').appendTo(parent)[0];

	//jumps to a more active part of the animation initially (just looks cooler especially when the preloader isn't displayed for very long)
	var radius = options.radius || 42, dotSize = options.dotSize || 15,	animationOffset = options.animationOffset || 1.8;

	var createDot = function(rotation) {

		var dot = $('<div />').appendTo(parent)[0];
		element.appendChild(dot);

		TweenLite.set(dot, {
			width:dotSize, 
			height:dotSize, 
			transformOrigin:(-radius + 'px 0px'), 
			x: radius, 
			backgroundColor:colors[colors.length-1], 
			borderRadius:'50%', 
			force3D:true, 
			position:'absolute', 
			rotation:rotation
		});

		dot.className = options.dotClass || 'preloader-dot';
		return dot;
	};

	var i = options.dotCount || 10;
	var rotationIncrement = 360 / i;
	var colors = options.colors || ['#61AC27','black']; // jshint ignore:line
	var animation = new TimelineLite({paused:true});
	var dots = [];
	var isActive = false;
	var box =  $('<div />').appendTo(parent)[0];
	var tl, dot, closingAnimation, j;

	colors.push(colors.shift());

	//setup background box/
	TweenLite.set(box, {
		width: radius * 2 + 50, 
		height: radius * 2 + 50, 
		position:'absolute', 
		xPercent:-50, 
		yPercent:-50, 
		opacity:((options.boxOpacity !== null) ? options.boxOpacity : 0.3)
	});
	
	box.className = options.boxClass || 'preloader-box';
	element.appendChild(box);
	parent.appendChild(element);

	TweenLite.set(element, {
		position:'absolute', 
		top:'45%', 
		left:'50%', 
		perspective:600, 
		overflow:'visible', 
		zIndex:2000
	});

	animation.from(box, 0.1, {opacity:0, scale:0.1, ease:Power1.easeOut}, animationOffset);

	while (--i > -1) {

		dot = createDot(i * rotationIncrement);
		dots.unshift(dot);
		
		animation.from(
			dot, 
			0.1, 
			{
				scale:0.01, 
				opacity:0, 
				ease:Power1.easeOut
			}, 
			animationOffset
		);
		
		//tuck the repeating parts of the animation into a nested TimelineMax (the intro shouldn't be repeated)
		tl = new TimelineMax({
			repeat:-1, 
			repeatDelay:0.25
		});

		for (j = 0; j < colors.length; j++) {
			tl.to(dot, 2.5, {rotation:'-=360', ease:Power2.easeInOut}, j * 2.9)
			.to(dot, 1.2, {skewX:'+=360', backgroundColor:colors[j], ease:Power2.easeInOut}, 1.6 + 2.9 * j);
		}
		//stagger its placement into the master timeline
		animation.add(tl, i * 0.07);
	}

	if (TweenLite.render) {

		//trigger the from() tweens' lazy-rendering 
		//(otherwise it'd take one tick to render everything in the beginning state, 
		//thus things may flash on the screen for a moment initially). 
		//There are other ways around this, but TweenLite.render() is probably the simplest in this case.
		TweenLite.render();
	}

	//call preloader.active(true) to open the preloader, preloader.active(false) to close it, or preloader.active() to get the current state.
	this.active = function(show) {
		if (!arguments.length) {
			return isActive;
		}

		if (isActive !== show) {
			isActive = show;
			if (closingAnimation) {
				//in case the preloader is made active/inactive/active/inactive really fast and there's still a closing animation running, kill it.
				closingAnimation.kill();
			}
			if (isActive) {
				element.style.visibility = 'visible';
				TweenLite.set([element, box], {rotation:0});
				animation.play(animationOffset);
			} else {
				closingAnimation = new TimelineLite();
				if (animation.time() < animationOffset + 0.3) {
					animation.pause();
					closingAnimation.to(element, 1, {rotation:-360, ease:Power1.easeInOut})
					.to(box, 1, {rotation:360, ease:Power1.easeInOut}, 0);
				}
				closingAnimation.staggerTo(dots, 0.3, {scale:0.01, opacity:0, ease:Power1.easeIn, overwrite:false}, 0.05, 0)
				.to(box, 0.4, {opacity:0, scale:0.2, ease:Power2.easeIn, overwrite:false}, 0)
				.call(function() { animation.pause(); closingAnimation = null; })
				.set(element, {visibility:'hidden'});
			}
		}
		return this;
	};
};

exports.services.AnimacaoLoader = AnimacaoLoader;