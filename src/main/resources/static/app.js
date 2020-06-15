$(document).ready(function(){
    var uploadAlert = $('#upload_info');
    uploadAlert.hide();
    $("form").submit(function(event){

        event.preventDefault();
        var file = document.getElementById('file');
        var security = $('#security').val();
        var formData = new FormData(); // Currently empty
        formData.append('security',security);
        formData.append('file',file.files[0]);


        $.ajax({
            xhr: function() {
                var xhr = new window.XMLHttpRequest();
                xhr.upload.addEventListener('progress',function(e){
                    console.log(e);
                    if(e.lengthComputable) {
                        console.log('bytes loaded: ',e.loaded);
                        console.log('total size: ',e.total);

                        console.log('uploaded Percent: ',(e.loaded/e.total));
                        var percent = Math.round((e.loaded/e.total)*100);
                        $('#progressBar').attr('aria-valuenow', percent).css('width', percent + '%').text(percent + '% Complete');
                    }
                });
                return xhr;
            },
            type: 'POST',
            url: '/',
            data: formData,
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false,
            success: function(res){
                console.log("success",JSON.stringify(res));
               uploadAlert.show();
                if(res.isReplaced === "true") {
                    uploadAlert.addClass('panel panel-info');
                } else {
                    uploadAlert.addClass('panel panel-success');
                }
                $('#p_msg').text(res.message);

                 setTimeout(function(){
                    location.reload();
                 }, 4000);
            }
        });
    });
});