<%@ tag pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!-- Modals -->

<div class="modal fade modal-level-plus-1" data-backdrop="static" id="commonUploadModal">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header move-cursor"">
				<h5 class="modal-title">
					${cmd_upload}
					<span class="font-weight-light pl-1"><span name="subtitle"></span>
				</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
            </div>
            <div class="modal-body p-2">
				<div class='d-flex align-items-center justify-content-center py-4'>${wait_plaseWait}</div>
            </div>
        </div>
    </div>
</div>

<!-- / Modals -->


<!--  Scripts -->

<script>

var commonUploadType = null;

$(document).ready(function() {

	$('#commonUploadModal').on('show.bs.modal', function (e) {
		console.log("${uploadOpenUrl}?type=" + commonUploadType);

	    $(this).find('.modal-body').load("${uploadOpenUrl}?type=" + commonUploadType);

		setTimeout(function(){
			$('.modal-backdrop:last-child').addClass('modal-level-plus-1');
		});

	});
	
	$('#commonUploadModal').on('hidden.bs.modal', function (e) {

		uploadModalClosed();

	});
});


function openUploadModal(type, title) {
	
	if (type) {
		commonUploadType = type;
	}
	
	if (commonUploadType) {
		$("#commonUploadModal span[name='subtitle']").text(title);
	    $("#commonUploadModal .modal-body").html("<div class='d-flex align-items-center justify-content-center py-4'>${wait_plaseWait}</div>");
	    
		$('#commonUploadModal .modal-dialog').draggable({ handle: '.modal-header' });
		$("#commonUploadModal").modal();
	}

}

</script>

<!--  / Scripts -->
