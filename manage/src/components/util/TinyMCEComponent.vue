<template>
    <div :class="{fullscreen:fullscreen}" class="tinymce-container"
         :style="{width:containerWidth ,height:containerHeight}">
        <textarea :id="tinymceId" class="tinymce-textarea"/>
    </div>
</template>

<script>

import load from "./dynamicLoadScript";

const tinymceCDN = "/tinymce/tinymce.min.js";

export default {
    name: "TinymceComponent",
    props: {
        id: {
            type: String,
            default: function () {
                return "vue-tinymce-" + +new Date() + ((Math.random() * 1000).toFixed(0) + "");
            }
        },
        value: {
            type: String,
            default: ""
        },
        toolbar: {
            type: Array,
            default() {
                return [
                    "searchreplace bold italic underline strikethrough alignleft aligncenter alignright outdent indent  blockquote undo redo removeformat subscript superscript code codesample",
                    "hr bullist numlist link charmap preview anchor pagebreak insertdatetime media table emoticons forecolor backcolor fullscreen lineheight"
                ];
            }
        },
        plugins: {
            type: String,
            default() {
                return "autoresize advlist anchor autolink autosave code codesample fullscreen insertdatetime link lists media nonbreaking pagebreak preview save searchreplace table visualblocks visualchars wordcount";
            }
        },
        menubar: {
            type: String,
            default: "file edit insert view format table"
        },
        height: {
            type: [Number, String],
            required: false,
            default: 360
        },
        width: {
            type: [Number, String],
            required: false,
            default: "auto"
        },
    },
    data() {
        return {
            hasChange: false,
            hasInit: false,
            tinymceId: this.id,
            fullscreen: false,
            language: "zh-Hans"
        };
    },
    computed: {

        containerWidth() {
            const width = this.width;
            if (/^[\d]+(\.[\d]+)?$/.test(width)) {
                // matches `100`, `'100'`
                return `${width}px`;
            }
            return width;
        },
        containerHeight() {
            const height = this.height;
            if (/^[\d]+(\.[\d]+)?$/.test(height)) {
                // matches `100`, `'100'`
                return `${height}px`;
            }
            return height;
        }
    },
    watch: {
        value(val) {
            if (!this.hasChange && this.hasInit) {
                this.$nextTick(() => window.tinymce.get(this.tinymceId).setContent(val || ""));
            }
        },
        language() {
            this.destroyTinymce();
            this.$nextTick(() => this.initTinymce());
        }
    },
    mounted() {
        this.init();
    },
    activated() {
        if (window.tinymce) {
            this.initTinymce();
        }
    },
    deactivated() {
        this.destroyTinymce();
    },
    destroyed() {
        this.destroyTinymce();
    },
    methods: {
        init() {
            // dynamic load tinymce from cdn
            load(tinymceCDN, err => {
                if (err) {
                    this.$message.error(err.message);
                    return;
                }
                this.initTinymce();
            });
        },
        initTinymce() {
            const _this = this;
            window.tinymce.init({
                language: this.language,
                convert_urls: false, // 上传图片使用全部路径
                selector: `#${this.tinymceId}`,
                height: this.height,
                body_class: "panel-body ",
                object_resizing: false,
                min_height: this.height,
                toolbar: this.toolbar,
                menubar: this.menubar,
                plugins: this.plugins,
                end_container_on_empty_block: true,
                powerpaste_word_import: "clean",
                code_dialog_height: 450,
                statusbar: false,
                code_dialog_width: 1000,
                advlist_bullet_styles: "square",
                advlist_number_styles: "default",
                imagetools_cors_hosts: ["www.tinymce.com", "codepen.io"],
                default_link_target: "_blank",
                link_title: false,
                nonbreaking_force_tab: true, // inserting nonbreaking space &nbsp; need Nonbreaking Space Plugin
                init_instance_callback: editor => {
                    if (_this.value) {
                        editor.setContent(_this.value);
                    }
                    _this.hasInit = true;
                    editor.on("NodeChange Change KeyUp SetContent", () => {
                        this.hasChange = true;
                        this.$emit("input", editor.getContent());
                    });
                    // editor.on("paste", evt => {
                    //   // 监听粘贴事件
                    //   this.onPaste(evt);
                    // });
                },
                setup(editor) {
                    editor.on("FullscreenStateChanged", e => {
                        _this.fullscreen = e.state;
                    });
                }
            });
        },
        destroyTinymce() {
            const tinymce = window.tinymce.get(this.tinymceId);
            if (this.fullscreen) {
                tinymce.execCommand("mceFullScreen");
            }

            if (tinymce) {
                tinymce.destroy();
            }
        },
        setContent(value) {
            window.tinymce.get(this.tinymceId).setContent(value);
        },
        getContent() {
            window.tinymce.get(this.tinymceId).getContent();
        },
    }
};
</script>

<style scoped>
.tinymce-container {
    position: relative;
    line-height: normal;
}

.tinymce-container >>> .mce-fullscreen {
    z-index: 10000;
}

.tinymce-textarea {
    visibility: hidden;
    z-index: -1;
}

.editor-custom-btn-container {
    position: absolute;
    right: 4px;
    top: 4px;
    z-index: 2100;
}

.fullscreen .editor-custom-btn-container {
    z-index: 10000;
    position: fixed;
}

.editor-upload-btn {
    display: inline-block;
}
</style>
