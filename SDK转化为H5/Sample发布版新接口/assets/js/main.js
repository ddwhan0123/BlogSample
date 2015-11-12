(function($) {
  $(function() {      

    // Table scroll
    $('.scrollable').slimScroll({
      height: '120px'
    });
	
	  // POPOVER & TOOLTIP
    $("[rel='popover']").popover();
    $("[rel='tooltip']").tooltip();
	
	 // PROGRESS-BAR
    $(window).ready(function(e){
        $.each($('div.progress-bar'),function(){
          $(this).css('width', $(this).attr('aria-valuetransitiongoal')+'%');
        });
    });

  });
  
})(jQuery);

(function( $ ) {

	'use strict';

	var initBasic = function() {
		new GMaps({
			div: '#gmap-basic',
			lat: -37.479217,
			lng: -72.351838
		});
	};

	var initBasicWithMarkers = function() {
		var map = new GMaps({
			div: '#gmap-basic-marker',
			lat: -37.479217,
			lng: -72.351838,
			markers: [{
				lat: -37.479217,
				lng: -72.351838,
				infoWindow: {
					content: ''
				}
			}]
		});

		map.addMarker({
			lat: -37.479217,
			lng: -72.351838,
			infoWindow: {
				content: ''
			}
		});
	};

	var initStatic = function() {
		var url = GMaps.staticMapURL({
			size: [725, 500],
			lat: -37.479217,
			lng: -72.351838,
			scale: 1
		});

		$('#gmap-static')
			.css({
				backgroundImage: 'url(' + url + ')',
				backgroundSize: 'cover'
			});
	};

	var initContextMenu = function() {
		var map = new GMaps({
			div: '#gmap-context-menu',
			lat: -37.479217,
			lng: -72.351838
		});

		map.setContextMenu({
			control: 'map',
			options: [
				{
					title: 'Add marker',
					name: 'add_marker',
					action: function(e) {
						this.addMarker({
							lat: e.latLng.lat(),
							lng: e.latLng.lng(),
							title: 'New marker'
						});
					}
				},
				{
					title: 'Center here',
					name: 'center_here',
					action: function(e) {
						this.setCenter(e.latLng.lat(), e.latLng.lng());
					}
				}
			]
		});
	};

	var initStreetView = function() {
		var gmap = GMaps.createPanorama({
			el: '#gmap-street-view',
			lat : -37.47921659282566,
			lng : -72.35183765820307
		});

		$(window).on( 'sidebar-left-toggle', function() {
			google.maps.event.trigger( gmap, 'resize' );
		});
	};

	// auto initialize
	$(function() {

		initBasic();
		initBasicWithMarkers();
		initStatic();
		initContextMenu();
		initStreetView();

	});

}).apply(this, [ jQuery ]);