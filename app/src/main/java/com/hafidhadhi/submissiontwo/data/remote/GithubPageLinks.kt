package com.hafidhadhi.submissiontwo.data.remote

import okhttp3.Response

class GithubPageLinks(response: Response) {

    var first: String? = null
    var last: String? = null
    var next: String? = null
    var prev: String? = null

    companion object {
        private const val DELIM_LINKS = "," //$NON-NLS-1$
        private const val DELIM_LINK_PARAM = ";" //$NON-NLS-1$
        private const val HEADER_LINK = "link"
        private const val META_REL = "rel"
        private const val META_FIRST = "first"
        private const val META_LAST = "last"
        private const val META_NEXT = "next"
        private const val META_PREV = "prev"
    }

    init {
        val linkHeader: String? = response.header(HEADER_LINK)
        if (linkHeader != null) {
            val links = linkHeader.split(DELIM_LINKS.toRegex()).toTypedArray()
            for (link in links) {
                val segments = link.split(DELIM_LINK_PARAM.toRegex()).toTypedArray()
                if (segments.size < 2) continue
                var linkPart = segments[0].trim { it <= ' ' }
                if (!linkPart.startsWith("<") || !linkPart.endsWith(">")) //$NON-NLS-1$ //$NON-NLS-2$
                    continue
                linkPart = linkPart.substring(1, linkPart.length - 1)
                for (i in 1 until segments.size) {
                    val rel = segments[i].trim { it <= ' ' }.split("=".toRegex())
                        .toTypedArray() //$NON-NLS-1$
                    if (rel.size < 2 || META_REL != rel[0]) continue
                    var relValue = rel[1]
                    if (relValue.startsWith("\"") && relValue.endsWith("\"")) //$NON-NLS-1$ //$NON-NLS-2$
                        relValue = relValue.substring(1, relValue.length - 1)
                    when (relValue) {
                        META_FIRST -> first = linkPart
                        META_LAST -> last = linkPart
                        META_NEXT -> next = linkPart
                        META_PREV -> prev = linkPart
                    }
                }
            }
        }
    }
}