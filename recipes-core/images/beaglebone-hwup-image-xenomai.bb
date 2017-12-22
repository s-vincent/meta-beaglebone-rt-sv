# Base this image on core-image-minimal
include recipes-core/images/core-image-minimal.bb

python () {                                                                     
    if d.getVar("PREFERRED_PROVIDER_virtual/kernel") != "linux-yocto-ipipe":
        raise bb.parse.SkipPackage("Set PREFERRED_PROVIDER_virtual/kernel to linux-yocto-ipipe to enable it")
}

DESCRIPTION = "A BeagleBone Black image with I-pipe/Xenomai kernel and \
               real-time test suite"

IMAGE_INSTALL += " \
	                kernel-modules \
                  rt-tests \
                  hwlatdetect \
                  ntp \
                  xenomai \
                 "
#IMAGE_FEATURES += "package-management"

