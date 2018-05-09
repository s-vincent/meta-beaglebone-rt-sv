require recipes-kernel/linux/linux-yocto.inc

# Skip processing of this recipe if it is not explicitly specified as the
# PREFERRED_PROVIDER for virtual/kernel. This avoids errors when trying
# to build multiple virtual/kernel providers, e.g. as dependency of
# core-image-rt-sdk, core-image-rt.
python () {
    if d.getVar("PREFERRED_PROVIDER_virtual/kernel") != "linux-yocto-rt":
        raise bb.parse.SkipPackage("Set PREFERRED_PROVIDER_virtual/kernel to linux-yocto-rt to enable it")
}

SRCREV ?= "fc72a4171174dd6b7ddefe5eeaa12cec9a162704"
LINUX_VERSION ?= "4.14.40"
PV = "${LINUX_VERSION}"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux-stable.git;branch=linux-4.14.y \
           file://defconfig \
           file://patch-4.14.39-rt29.patch \
          "

KCONF_BSP_AUDIT_LEVEL = "2"
LINUX_KERNEL_TYPE = "preempt-rt"

COMPATIBLE_MACHINE = "(qemux86|qemux86-64|qemuarm|qemuppc|qemumips)"
COMPATIBLE_MACHINE_beaglebone = "beaglebone"

KERNEL_DEVICETREE_qemuarm = "versatile-pb.dtb"

# Functionality flags
#KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc features/taskstats/taskstats.scc"
#KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
#KERNEL_FEATURES_append_qemuall=" cfg/virtio.scc"
#KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
#KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
