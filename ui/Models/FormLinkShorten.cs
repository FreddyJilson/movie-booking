using System;

namespace ui.Models {
    public class FormLinkShorten {
        public String link { get; set; }

        public FormLinkShorten() {
            link = "";
        }
        public FormLinkShorten(String _link) {
            link = _link;
        }
    }
}