package de.fred4jupiter.fredbet.web.admin;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import de.fred4jupiter.fredbet.security.FredBetPermission;
import de.fred4jupiter.fredbet.service.excel.ReportService;

@Controller
@RequestMapping("/excelexport")
@PreAuthorize("hasAuthority('" + FredBetPermission.PERM_ADMINISTRATION + "')")
public class ExcelExportController {

    private static final String CONTENT_TYPE_EXCEL = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    @Autowired
    private ReportService reportService;

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public ModelAndView showCachePage() {
        return new ModelAndView("admin/excel_export");
    }

    @RequestMapping(value = "/allBets", method = RequestMethod.GET, produces = CONTENT_TYPE_EXCEL)
    public ResponseEntity<byte[]> exportAllBets(HttpServletResponse response) {
        final String fileName = "allBets.xlsx";
        byte[] fileContent = this.reportService.exportBetsToExcel(LocaleContextHolder.getLocale());
        if (fileContent == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return createResponseEntity(fileName, fileContent);
    }
    
    @RequestMapping(value = "/extraBets", method = RequestMethod.GET, produces = CONTENT_TYPE_EXCEL)
    public ResponseEntity<byte[]> exportExtraBets(HttpServletResponse response) {
        final String fileName = "extraBets.xlsx";
        byte[] fileContent = this.reportService.exportExtraBetsToExcel(LocaleContextHolder.getLocale());
        if (fileContent == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return createResponseEntity(fileName, fileContent);
    }

    @RequestMapping(value = "/pointsCount", method = RequestMethod.GET, produces = CONTENT_TYPE_EXCEL)
    public ResponseEntity<byte[]> exportPointsCountByUser(HttpServletResponse response) {
        final String fileName = "numberOfReachedPointsCount.xlsx";
        byte[] fileContent = this.reportService.exportNumberOfPointsInBets(LocaleContextHolder.getLocale());
        if (fileContent == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return createResponseEntity(fileName, fileContent);
    }

    @RequestMapping(value = "/ranking", method = RequestMethod.GET, produces = CONTENT_TYPE_EXCEL)
    public ResponseEntity<byte[]> exportRanking(HttpServletResponse response) {
        final String fileName = "ranking.xlsx";
        byte[] fileContent = this.reportService.exportRankingToExcel(LocaleContextHolder.getLocale());
        if (fileContent == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return createResponseEntity(fileName, fileContent);
    }

    private ResponseEntity<byte[]> createResponseEntity(final String fileName, byte[] fileContent) {
        return ResponseEntity.ok().header("Content-Type", CONTENT_TYPE_EXCEL)
                .header("Content-Disposition", "inline; filename=\"" + fileName + "\"").body(fileContent);
    }
}
