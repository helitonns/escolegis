/** 
 * PrimeFaces Sapphire Layout
 */
PrimeFaces.widget.Sapphire = PrimeFaces.widget.BaseWidget.extend({
    
    init: function(cfg) {
        this._super(cfg);
        this.wrapper = $(document.body).children('.layout-container');
        this.layoutMenuContainer = $('#layout-menu-container');
        this.menu = this.jq;
        this.menulinks = this.menu.find('a');
        this.menuButton = $('#layout-menubutton');
        this.profileMenuButton = $('#profile-menu-button');
        this.profileMenu = $('#layout-profile-menu');
        this.configButton = $('#layout-config-button');
        this.configMenu = $('#layout-config');
        this.configMenuClose = this.configMenu.find('> .layout-config-content > .layout-config-close');
        this.nano = $('#layout-nano'); 
        
        this.bindEvents();

        if (!this.isHorizontal()) {
            this.restoreMenuState();
        }
        
        this.expandedMenuitems = this.expandedMenuitems||[];
    },
    
    bindEvents: function() {
        var $this = this;

        this.nano.nanoScroller({flash: true});

        this.layoutMenuContainer.on('click', function() {
            $this.menuClick = true;
        });

        this.menuButton.off('click').on('click', function(e) {
            $this.menuClick = true;
            $this.wrapper.toggleClass('layout-menu-active');
            $this.menuButton.toggleClass('layout-menubutton-active');

            $(document.body).toggleClass('blocked-scroll');
                
            setTimeout(function () {
                $this.nano.nanoScroller();
                //$this.nanoScrollContent.removeAttr('tabindex');
            }, 450);

            e.preventDefault();
        });

        this.menulinks.off('click').on('click', function(e) {
            var link = $(this),
            item = link.parent(),
            submenu = item.children('div');
            horizontal = $this.isHorizontal();
            
            if (horizontal) {
                $this.horizontalMenuClick = true;
            }
            
            if (item.hasClass('active-menuitem')) {
                if (submenu.length) {
                    $this.removeMenuitem(item.attr('id'));
                    item.removeClass('active-menuitem');
                    
                    if (horizontal) {
                        if(item.parent().is($this.jq)) {
                            $this.menuActive = false;
                        }
                        
                        submenu.hide();
                    }
                    else {
                        submenu.slideUp();
                    }
                    
                    submenu.find('.ink').remove();
                }
            }
            else {
                $this.addMenuitem(item.attr('id'));

                if (horizontal) {
                    $this.deactivateItems(item.siblings());
                    $this.activate(item);
                    $this.menuActive = true;
                }
                else {
                    $this.deactivateItems(item.siblings(), true);
                    $this.activate(item);
                }
            }

            setTimeout(function() {
                $this.nano.nanoScroller();
            }, 500);
                                    
            if (!horizontal) {
                setTimeout(function() {
                    $(".nano").nanoScroller();
                }, 500);
            }
            
            if (submenu.length) {
                e.preventDefault();
            }
            else {
                if ($this.isHorizontal())
                    $this.resetHorizontalMenu();
                else
                    $this.hideVerticalMenu();
            }
        });

        this.menu.find('> li').on('mouseenter', function(e) {
            if($this.isHorizontal()) {
                var item = $(this);
                
                if(!item.hasClass('active-menuitem')) {
                    var activeItem = item.siblings('.active-menuitem');
                    activeItem.removeClass('active-menuitem');
                    if (activeItem.find('> .layout-submenu-container > .layout-submenu').hasClass('layout-megamenu')) {
                        activeItem.children('.layout-submenu-container').hide();
                    }
                    else {
                        activeItem.find('.layout-submenu-container:visible').hide();
                    }
                    $this.menu.find('.ink').remove();
                    
                    if($this.menuActive) {
                        item.addClass('active-menuitem');
                        item.children('.layout-submenu-container').show();
                    }
                }
            }
        });

        this.profileMenuButton.on('click', function(e) {
            $this.profileMenuClick = true;
            $this.profileMenu.removeClass('fadeInDown fadeOutUp');
            $this.profileMenu.find('.ink').remove();

            if($this.profileMenu.hasClass('layout-profile-menu-active'))
                $this.hideProfileMenu();
            else
                $this.profileMenu.addClass('layout-profile-menu-active fadeInDown');

            e.preventDefault();
        });

        this.profileMenu.on('click', function() {
            $this.profileMenuClick = true;
        });

        this.profileMenu.find('a').on('click', function() {
            $this.hideProfileMenu();
        });
         
        this.configButton.on('click', function(e) {
            $this.configMenu.find('.ink').remove();
            $this.configMenu.removeClass('layout-config-exit-done').addClass('layout-config-enter');
            setTimeout(function() {
                $this.configMenu.addClass('layout-config-enter-active');
            }, 1);

            setTimeout(function() {
                $this.configMenu.removeClass('layout-config-enter layout-config-enter-active').addClass('layout-config-enter-done')
            }, 150);

            $(document.body).addClass('blocked-scroll-config').append('<div class="layout-config-mask"></div>');
        
            e.preventDefault();
        });

        this.configMenuClose.on('click', function(e) {
            $this.configMenu.removeClass('layout-config-enter-done').addClass('layout-config-exit');
            setTimeout(function() {
                $this.configMenu.addClass('layout-config-exit-active');
            }, 1);

            setTimeout(function() {
                $this.configMenu.removeClass('layout-config-exit layout-config-exit-active').addClass('layout-config-exit-done')
            }, 150);

            $(document.body).removeClass('blocked-scroll-config').children('.layout-config-mask').remove();
        
            e.preventDefault();
        });
        
        $(document.body).off('click').on('click', function() {
            if ($this.isHorizontal() && !$this.horizontalMenuClick) {
                $this.resetHorizontalMenu();
            }

            if (!$this.profileMenuClick) {
                $this.hideProfileMenu();
            }

            if (!$this.menuClick) {
                $this.hideVerticalMenu();
            }
                    
            $this.menuClick = false;
            $this.horizontalMenuClick = false;
            $this.profileMenuClick = false;
        });
    },

    resetHorizontalMenu: function() {
        this.menu.find('.active-menuitem').removeClass('active-menuitem');
        this.menu.find('.layout-submenu-container:not(.layout-submenu-megamenu-container):visible').hide();
        this.menuActive = false;
    },

    hideVerticalMenu: function() {
        this.wrapper.removeClass('layout-menu-active');
        this.menuButton.removeClass('layout-menubutton-active');
        $(document.body).removeClass('blocked-scroll');
    },

    hideProfileMenu: function() {
        var $this = this;
        this.profileMenu.addClass('fadeOutUp').removeClass('fadeInDown');
                
        setTimeout(function() {
            $this.profileMenu.removeClass('layout-profile-menu-active fadeOutUp');
        }, 150);
    },
        
    activate: function(item) {
        var submenu =  item.children('div');
        item.addClass('active-menuitem');

        if (submenu.length) {
            if (this.isHorizontal())
                submenu.show();
            else
                submenu.slideDown();
        }
    },
    
    deactivate: function(item) {
        var submenu =  item.children('div');
        item.removeClass('active-menuitem').find('.ink').remove();
        
        if(submenu.length) {
            submenu.hide();
            submenu.find('.ink').remove();
        }
    },

    deactivateItems: function(items, animate) {
        var $this = this;
        
        for(var i = 0; i < items.length; i++) {
            var item = items.eq(i),
            submenu = item.children('div');
            
            if (submenu.length) {
                if (item.hasClass('active-menuitem')) {
                    var activeSubItems = item.find('.active-menuitem');
                    item.removeClass('active-menuitem');
                    
                    if (animate) {
                        submenu.slideUp('normal', function() {
                            $(this).parent().find('.active-menuitem').each(function() {
                                $this.deactivate($(this));
                            });
                        });
                    }
                    else {
                        submenu.hide();
                        item.find('.active-menuitem').each(function() {
                            $this.deactivate($(this));
                        });
                    }
                    
                    $this.removeMenuitem(item.attr('id'));
                    activeSubItems.each(function() {
                        $this.removeMenuitem($(this).attr('id'));
                    });
                }
                else {
                    item.find('.active-menuitem').each(function() {
                        var subItem = $(this);
                        $this.deactivate(subItem);
                        $this.removeMenuitem(subItem.attr('id'));
                    });
                }
            }
            else if (item.hasClass('active-menuitem')) {
                $this.deactivate(item);
                $this.removeMenuitem(item.attr('id'));
            }
        }
    },
    
    removeMenuitem: function (id) {
        this.expandedMenuitems = $.grep(this.expandedMenuitems, function (value) {
            return value !== id;
        });
        this.saveMenuState();
    },
    
    addMenuitem: function (id) {
        if ($.inArray(id, this.expandedMenuitems) === -1) {
            this.expandedMenuitems.push(id);
        }
        this.saveMenuState();
    },
    
    saveMenuState: function() {
        if (!this.isHorizontal()) {            
            $.cookie('sapphire_expandeditems', this.expandedMenuitems.join(','), {path: '/'});
        }
    },
    
    clearMenuState: function() {
        $.removeCookie('sapphire_expandeditems', {path: '/'});
    },
    
    restoreMenuState: function() {
        var menuCookie = $.cookie('sapphire_expandeditems');
        if (menuCookie) {
            this.expandedMenuitems = menuCookie.split(',');
            for (var i = 0; i < this.expandedMenuitems.length; i++) {
                var id = this.expandedMenuitems[i];
                if (id) {
                    var menuitem = $("#" + this.expandedMenuitems[i].replace(/:/g, "\\:"));
                    menuitem.addClass('active-menuitem');
                    
                    var submenu = menuitem.children('div');
                    if(submenu.length) {
                        submenu.show();
                    }
                }
            }
        }
    },
        
    isMobile: function() {
        return window.innerWidth <= 1024;
    },

    isDesktop: function() {
        return window.innerWidth > 1024;
    },

    isHorizontal: function() {
        return this.wrapper.hasClass('layout-menu-horizontal') && this.isDesktop();
    },

    isVertical: function() {
        return this.wrapper.hasClass('layout-menu-vertical');
    }
});

/*!
 * jQuery Cookie Plugin v1.4.1
 * https://github.com/carhartl/jquery-cookie
 *
 * Copyright 2006, 2014 Klaus Hartl
 * Released under the MIT license
 */
(function (factory) {
	if (typeof define === 'function' && define.amd) {
		// AMD (Register as an anonymous module)
		define(['jquery'], factory);
	} else if (typeof exports === 'object') {
		// Node/CommonJS
		module.exports = factory(require('jquery'));
	} else {
		// Browser globals
		factory(jQuery);
	}
}(function ($) {

	var pluses = /\+/g;

	function encode(s) {
		return config.raw ? s : encodeURIComponent(s);
	}

	function decode(s) {
		return config.raw ? s : decodeURIComponent(s);
	}

	function stringifyCookieValue(value) {
		return encode(config.json ? JSON.stringify(value) : String(value));
	}

	function parseCookieValue(s) {
		if (s.indexOf('"') === 0) {
			// This is a quoted cookie as according to RFC2068, unescape...
			s = s.slice(1, -1).replace(/\\"/g, '"').replace(/\\\\/g, '\\');
		}

		try {
			// Replace server-side written pluses with spaces.
			// If we can't decode the cookie, ignore it, it's unusable.
			// If we can't parse the cookie, ignore it, it's unusable.
			s = decodeURIComponent(s.replace(pluses, ' '));
			return config.json ? JSON.parse(s) : s;
		} catch(e) {}
	}

	function read(s, converter) {
		var value = config.raw ? s : parseCookieValue(s);
		return $.isFunction(converter) ? converter(value) : value;
	}

	var config = $.cookie = function (key, value, options) {

		// Write

		if (arguments.length > 1 && !$.isFunction(value)) {
			options = $.extend({}, config.defaults, options);

			if (typeof options.expires === 'number') {
				var days = options.expires, t = options.expires = new Date();
				t.setMilliseconds(t.getMilliseconds() + days * 864e+5);
			}

			return (document.cookie = [
				encode(key), '=', stringifyCookieValue(value),
				options.expires ? '; expires=' + options.expires.toUTCString() : '', // use expires attribute, max-age is not supported by IE
				options.path    ? '; path=' + options.path : '',
				options.domain  ? '; domain=' + options.domain : '',
				options.secure  ? '; secure' : ''
			].join(''));
		}

		// Read

		var result = key ? undefined : {},
			// To prevent the for loop in the first place assign an empty array
			// in case there are no cookies at all. Also prevents odd result when
			// calling $.cookie().
			cookies = document.cookie ? document.cookie.split('; ') : [],
			i = 0,
			l = cookies.length;

		for (; i < l; i++) {
			var parts = cookies[i].split('='),
				name = decode(parts.shift()),
				cookie = parts.join('=');

			if (key === name) {
				// If second argument (value) is a function it's a converter...
				result = read(cookie, value);
				break;
			}

			// Prevent storing a cookie that we couldn't decode.
			if (!key && (cookie = read(cookie)) !== undefined) {
				result[name] = cookie;
			}
		}

		return result;
	};

	config.defaults = {};

	$.removeCookie = function (key, options) {
		// Must not alter options, thus extending a fresh object...
		$.cookie(key, '', $.extend({}, options, { expires: -1 }));
		return !$.cookie(key);
	};

}));

/* JS extensions to support material animations */
if(PrimeFaces.widget.InputSwitch) {
    PrimeFaces.widget.InputSwitch = PrimeFaces.widget.InputSwitch.extend({
         
         init: function(cfg) {
             this._super(cfg);
             
             if(this.input.prop('checked')) {
                 this.jq.addClass('ui-inputswitch-checked');
             }
         },
         
         toggle: function() {
             var $this = this;

             if(this.input.prop('checked')) {
                 this.uncheck(); 
                 setTimeout(function() {
                    $this.jq.removeClass('ui-inputswitch-checked');
                 }, 100);
             }
             else {
                 this.check();
                 setTimeout(function() {
                    $this.jq.addClass('ui-inputswitch-checked');
                 }, 100);
             }
         }
    });
}

if(PrimeFaces.widget.SelectBooleanButton) {
    PrimeFaces.widget.SelectBooleanButton.prototype.check = function() {
        if(!this.disabled) {
            this.input.prop('checked', true);
            this.jq.addClass('ui-state-active').children('.ui-button-text').contents()[0].textContent = this.cfg.onLabel;

            if(this.icon.length > 0) {
                this.icon.removeClass(this.cfg.offIcon).addClass(this.cfg.onIcon);
            }

            this.input.trigger('change');
        }
    }
    
    PrimeFaces.widget.SelectBooleanButton.prototype.uncheck = function() {
        if(!this.disabled) {
            this.input.prop('checked', false);
            this.jq.removeClass('ui-state-active').children('.ui-button-text').contents()[0].textContent = this.cfg.offLabel;

            if(this.icon.length > 0) {
                this.icon.removeClass(this.cfg.onIcon).addClass(this.cfg.offIcon);
            }

            this.input.trigger('change');
        }
    }
}

PrimeFaces.skinInput = function(input) {
    setTimeout(function() {
        if(input.val() != '') {
            var parent = input.parent();
            input.addClass('ui-state-filled');
            
            if(parent.is("span:not('.md-inputfield')")) {
                parent.addClass('md-inputwrapper-filled');
            }
        }
    }, 1);
    
    input.on('mouseenter', function() {
        $(this).addClass('ui-state-hover');
    })
    .on('mouseleave', function() {
        $(this).removeClass('ui-state-hover');
    })
    .on('focus', function() {
        var parent = input.parent();
        $(this).addClass('ui-state-focus');
        
        if(parent.is("span:not('.md-inputfield')")) {
            parent.addClass('md-inputwrapper-focus');
        }
    })
    .on('blur', function() {
        $(this).removeClass('ui-state-focus');

        if(input.hasClass('hasDatepicker')) {
            setTimeout(function() {
                PrimeFaces.onInputBlur(input);
            },150);
        }
        else {
            PrimeFaces.onInputBlur(input);
        }
    });

    //aria
    input.attr('role', 'textbox')
            .attr('aria-disabled', input.is(':disabled'))
            .attr('aria-readonly', input.prop('readonly'));

    if(input.is('textarea')) {
        input.attr('aria-multiline', true);
    }

    return this;
};

PrimeFaces.onInputBlur = function(input) {
    var parent = input.parent(),
    hasInputFieldClass = parent.is("span:not('.md-inputfield')");
    
    if(parent.hasClass('md-inputwrapper-focus')) {
        parent.removeClass('md-inputwrapper-focus');
    }
    
    if(input.val() != '') {
        input.addClass('ui-state-filled');
        if(hasInputFieldClass) {
            parent.addClass('md-inputwrapper-filled');
        }
    }
    else {
        input.removeClass('ui-state-filled');
        parent.removeClass('md-inputwrapper-filled');
    }    
};

if(PrimeFaces.widget.AutoComplete) {
    PrimeFaces.widget.AutoComplete.prototype.setupMultipleMode = function() {
        var $this = this;
        this.multiItemContainer = this.jq.children('ul');
        this.inputContainer = this.multiItemContainer.children('.ui-autocomplete-input-token');

        this.multiItemContainer.hover(function() {
                $(this).addClass('ui-state-hover');
            },
            function() {
                $(this).removeClass('ui-state-hover');
            }
        ).click(function() {
            $this.input.focus();
        });

        //delegate events to container
        this.input.focus(function() {
            $this.multiItemContainer.addClass('ui-state-focus');
            $this.jq.addClass('md-inputwrapper-focus');
        }).blur(function(e) {
            $this.multiItemContainer.removeClass('ui-state-focus');
            $this.jq.removeClass('md-inputwrapper-focus').addClass('md-inputwrapper-filled');
            
            setTimeout(function() {
                if($this.hinput.children().length == 0 && !$this.multiItemContainer.hasClass('ui-state-focus')) {
                    $this.jq.removeClass('md-inputwrapper-filled');
                }
            }, 150); 
        });

        var closeSelector = '> li.ui-autocomplete-token > .ui-autocomplete-token-icon';
        this.multiItemContainer.off('click', closeSelector).on('click', closeSelector, null, function(event) {
            if($this.multiItemContainer.children('li.ui-autocomplete-token').length === $this.cfg.selectLimit) {
                if(PrimeFaces.isIE(8)) {
                    $this.input.val('');
                }
                $this.input.css('display', 'inline');
                $this.enableDropdown();
            }
            $this.removeItem(event, $(this).parent());
        });
    };
};

if(PrimeFaces.widget.Calendar) {
    PrimeFaces.widget.Calendar.prototype.bindDateSelectListener = function() {
        var _self = this;

        this.cfg.onSelect = function() {
            if(_self.cfg.popup) {
                _self.fireDateSelectEvent();
            }
            else {
                var newDate = $.datepicker.formatDate(_self.cfg.dateFormat, _self.getDate());

                _self.input.val(newDate);
                _self.fireDateSelectEvent();
            }
            
            if(_self.input.val() != '') {
               var parent = _self.input.parent();
               parent.addClass('md-inputwrapper-filled');
               _self.input.addClass('ui-state-filled');
           }
        };
    };
}

/* Issue #924 is fixed for 5.3+ and 6.0. (compatibility with 5.3) */
if(window['PrimeFaces'] && window['PrimeFaces'].widget.Dialog) {
    PrimeFaces.widget.Dialog = PrimeFaces.widget.Dialog.extend({
        
        enableModality: function() {
            this._super();
            $(document.body).children(this.jqId + '_modal').addClass('ui-dialog-mask');
        },
        
        syncWindowResize: function() {}
    });
}

/* Issue #2131 */
if(window['PrimeFaces'] && window['PrimeFaces'].widget.Schedule) {
    PrimeFaces.widget.Schedule = PrimeFaces.widget.Schedule.extend({
        
        setupEventSource: function() {
            var $this = this,
            offset = moment().utcOffset()*60000;

            this.cfg.events = function(start, end, timezone, callback) {
                var options = {
                    source: $this.id,
                    process: $this.id,
                    update: $this.id,
                    formId: $this.cfg.formId,
                    params: [
                        {name: $this.id + '_start', value: start.valueOf() + offset},
                        {name: $this.id + '_end', value: end.valueOf() + offset}
                    ],
                    onsuccess: function(responseXML, status, xhr) {
                        PrimeFaces.ajax.Response.handle(responseXML, status, xhr, {
                                widget: $this,
                                handle: function(content) {
                                    callback($.parseJSON(content).events);
                                }
                            });

                        return true;
                    }
                };

                PrimeFaces.ajax.Request.handle(options);
            }
        }
    });
}

if(PrimeFaces.widget.SelectOneMenu) {
    PrimeFaces.widget.SelectOneMenu = PrimeFaces.widget.SelectOneMenu.extend({
        init: function(cfg) {
            this._super(cfg);

            var $this = this;
            if(!this.disabled && this.jq.parent().hasClass('md-inputfield')) {
                this.itemsContainer.children('.ui-selectonemenu-item:first').css({'display': 'none'});
                if (this.input.val() != "") {
                    this.jq.addClass("ui-state-filled");
                }

                this.input.off('change').on('change', function() {
                    $this.inputValueControl($(this));
                });
                
                if(this.cfg.editable) {
                    this.label.on('input', function(e) {
                        $this.inputValueControl($(this));
                    }).on('focus', function() {
                        $this.jq.addClass('ui-state-focus');
                    }).on('blur', function() {
                        $this.jq.removeClass('ui-state-focus');
                        $this.inputValueControl($(this));
                    });
                }
            }
        },
        
        inputValueControl: function(input) {
            if (input.val() != "")
                this.jq.addClass("ui-state-filled"); 
            else
                this.jq.removeClass("ui-state-filled");
        }
    });
}

PrimeFaces.SapphireConfigurator = {

    changePrimaryColor: function(newColor) {
        var linkElement = $('link[href*="layout-"');
        var href = linkElement.attr('href');
        var startIndexOf = href.indexOf('layout-') + 7;
        var endIndexOf = href.indexOf('.css');
        var currentColor = href.substring(startIndexOf, endIndexOf);
        linkElement.attr('href', href.replace(currentColor, newColor));
    },

    changeSectionTheme: function(theme, section) {
        var wrapperElement = $('.layout-container');
        var styleClass = wrapperElement.attr('class');
        var tokens = styleClass.split(' ');
        var sectionClass;
        for (var i = 0; i < tokens.length; i++) {
            if (tokens[i].indexOf(section + '-') > -1) {
                sectionClass = tokens[i];
                break;
            }
        }

        wrapperElement.attr('class', styleClass.replace(sectionClass, section + '-' + theme));
    },

    changeMenuToHorizontal: function(value) {
        $('.layout-container').toggleClass('layout-menu-horizontal');
    },

    changeComponentsTheme: function(theme) {
        var linkElement = $('link[href*="theme.css"');
        var href = linkElement.attr('href');
        var index = href.lastIndexOf('-') + 1;
        var currentTheme = href.substring(index);
        linkElement.attr('href', href.replace(currentTheme, theme));
    }
}