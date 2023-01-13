<%@ tag pageEncoding="UTF-8"%>


<!-- Page scripts  -->

<link rel="stylesheet" href="/resources/vendor/lib/blueimp-gallery/gallery.css">
<link rel="stylesheet" href="/resources/vendor/lib/blueimp-gallery/gallery-indicator.css">

<script src="/resources/vendor/lib/blueimp-gallery/gallery.js"></script>
<script src="/resources/vendor/lib/blueimp-gallery/gallery-fullscreen.js"></script>
<script src="/resources/vendor/lib/blueimp-gallery/gallery-indicator.js"></script>


<!--  Gallery lightbox template -->

<div id="imgLlightbox" class="blueimp-gallery blueimp-gallery-controls" style="z-index: 2200 !important;">
	<div class="slides"></div>
	<h3 class="title"></h3>
	<a class="prev">‹</a>
	<a class="next">›</a>
	<a class="close">×</a>
	<a class="play-pause"></a>
	<ol class="indicator"></ol>
</div>

<!--  / Gallery lightbox template -->


<!--  Scripts -->

<script>

function viewUnivImage(url) {
	
	blueimpGallery(
		[{
			data: {urls: [url]}
		}], {
    	container: '#imgLlightbox',
        urlProperty: 'data.urls[0]'
	});
}


function viewUnivImage5(url1, url2, url3, url4, url5) {
	
	var data = [];
	if (url1) { data.push({ data: { urls: [url1] } }); }
	if (url2) { data.push({ data: { urls: [url2] } }); }
	if (url3) { data.push({ data: { urls: [url3] } }); }
	if (url4) { data.push({ data: { urls: [url4] } }); }
	if (url5) { data.push({ data: { urls: [url5] } }); }

	if (data.length > 0) {
		blueimpGallery(
				data, {
		    	container: '#imgLlightbox',
		        urlProperty: 'data.urls[0]'
			});
	}
}


function viewUnivImages(links, urlProperty, index) {
	
	blueimpGallery(links, {
		container: '#imgLlightbox',
		carousel: false,
		index: index,
		urlProperty: urlProperty,
	});
}

</script>

<!--  / Scripts -->
