package com.capstone.capstone.map.controller;

import com.capstone.capstone.map.dto.DistanceDto;
import com.capstone.capstone.map.dto.DistanceRequestDto;
import com.capstone.capstone.map.dto.PredictedTimeDto;
import com.capstone.capstone.map.service.DistanceCalculatorService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DistanceApiController {

    private final static String URL = "https://dapi.kakao.com/v2/local/geo/transcoord.json";
    private final DistanceCalculatorService distanceCalculatorService;
    private final ObjectMapper objectMapper; // JSON 파싱을 위한 ObjectMapper

    @Value("${kakao.map.restapi-key}")
    private  String REST_API_KEY;
    private final RestTemplate restTemplate;



    @PostMapping("/api/distanceList")
    public ResponseEntity<DistanceDto> getDistanceList(@RequestBody List<Integer> distanceList) {
        log.info("distanceList : " + distanceList);
        DistanceDto distanceDto = new DistanceDto();
        for (Integer distance : distanceList) {
            distanceDto.addDistance(distance);
        }
        //log.info(distanceDto.getDistanceList().toString());
        return  ResponseEntity.ok().body(distanceDto);
    }


    @GetMapping("/convert/wgs84-to-wtm")
    public ResponseEntity<List<List<Double>>> wgs84ToWTM() {
        List<List<Double>> originalCoords = Arrays.asList(
                Arrays.asList(127.1008565722434, 37.396502014449894),
                Arrays.asList(127.10332041039379, 37.358794072637785),
                Arrays.asList(127.1033906630679, 37.345278879829046),
                Arrays.asList(127.1034515341123, 37.33356568720989),
                Arrays.asList(127.10348898526756, 37.32635755753728),
                Arrays.asList(127.1035591912988, 37.312842290450895),
                Arrays.asList(127.10367148061468, 37.291217797312754),
                Arrays.asList(127.10371825319847, 37.282207568764164),
                Arrays.asList(127.10375566507477, 37.27499937548597),
                Arrays.asList(127.10378839595677, 37.268692199713435),
                Arrays.asList(127.10381177258138, 37.26418706952283),
                Arrays.asList(127.10381177258138, 37.26418706952283),
                Arrays.asList(127.10590011218926, 37.237307608483775),
                Arrays.asList(127.10573924075685, 37.22862568813276),
                Arrays.asList(127.10000025955983, 37.220871613416406),
                Arrays.asList(127.09583359115491, 37.21257018267256),
                Arrays.asList(127.09583359115491, 37.21257018267256),
                Arrays.asList(127.09429176772097, 37.19856215954695),
                Arrays.asList(127.09606378065385, 37.18069128191127),
                Arrays.asList(127.09606378065385, 37.18069128191127),
                Arrays.asList(127.09610530105527, 37.17979204711072),
                Arrays.asList(127.09095628389771, 37.16515618993971),
                Arrays.asList(127.08860850799043, 37.160092823492406),
                Arrays.asList(127.08434548530306, 37.15080010058382),
                Arrays.asList(127.0849166721204, 37.141197247014425),
                Arrays.asList(127.0849166721204, 37.141197247014425),
                Arrays.asList(127.0849166721204, 37.141197247014425),
                Arrays.asList(127.0930069536072, 37.129810043833395),
                Arrays.asList(127.1030650593399, 37.122612242541166),
                Arrays.asList(127.11166903214135, 37.11556399674677),
                Arrays.asList(127.1192174917301, 37.09957860692084),
                Arrays.asList(127.12305254428085, 37.091117678841705),
                Arrays.asList(127.12649893485275, 37.083501406170555),
                Arrays.asList(127.1303810262686, 37.076033172993704),
                Arrays.asList(127.13500373639995, 37.06606044443592),
                Arrays.asList(127.13564829514779, 37.05887786188655),
                Arrays.asList(127.13638237752647, 37.050797951559026),
                Arrays.asList(127.13831062812542, 37.038306049966444),
                Arrays.asList(127.13831062812542, 37.038306049966444),
                Arrays.asList(127.13853955507483, 37.03742480458621),
                Arrays.asList(127.13853955507483, 37.03742480458621),
                Arrays.asList(127.13853955507483, 37.03742480458621),
                Arrays.asList(127.13853955507483, 37.03742480458621),
                Arrays.asList(127.1453124272744, 37.01470955711591),
                Arrays.asList(127.1453124272744, 37.01470955711591),
                Arrays.asList(127.14754523576136, 37.010580324282586),
                Arrays.asList(127.14995421096009, 37.00554343076114),
                Arrays.asList(127.1525524852754, 36.998650388044474),
                Arrays.asList(127.1525524852754, 36.998650388044474),
                Arrays.asList(127.15287585591632, 36.99778840807855),
                Arrays.asList(127.15417441331587, 36.99434172233534),
                Arrays.asList(127.15925352834235, 36.98342187402131),
                Arrays.asList(127.17639266951697, 36.96229970125497),
                Arrays.asList(127.18741093192104, 36.94872268259277),
                Arrays.asList(127.18931431709872, 36.935605221369116),
                Arrays.asList(127.18893841729852, 36.922106140377274),
                Arrays.asList(127.18900881479165, 36.913104727352874),
                Arrays.asList(127.18826863839793, 36.877147179016006),
                Arrays.asList(127.1869520831318, 36.869115008014944),
                Arrays.asList(127.18577033175691, 36.86197710846287),
                Arrays.asList(127.17617640844787, 36.84377692355856),
                Arrays.asList(127.16980237991844, 36.82776176570915),
                Arrays.asList(127.16980237991844, 36.82776176570915),
                Arrays.asList(127.16620375354104, 36.8193716654845),
                Arrays.asList(127.16519118693068, 36.80679540096083),
                Arrays.asList(127.1666213807959, 36.79974216728726),
                Arrays.asList(127.17423206856309, 36.78381344556787),
                Arrays.asList(127.17815649561776, 36.77349370895917),
                Arrays.asList(127.18276946573764, 36.76855065286801),
                Arrays.asList(127.19023650476807, 36.76697529740176),
                Arrays.asList(127.20029075200351, 36.76652505050319),
                Arrays.asList(127.21794733687643, 36.759564314202194),
                Arrays.asList(127.21794733687643, 36.759564314202194),
                Arrays.asList(127.21794733687643, 36.759564314202194),
                Arrays.asList(127.2309875538001, 36.745043810570984),
                Arrays.asList(127.25270704844826, 36.73662758152992),
                Arrays.asList(127.26533856658425, 36.73123218284996),
                Arrays.asList(127.28400196564496, 36.734109035151555),
                Arrays.asList(127.2975301370419, 36.73091179184357),
                Arrays.asList(127.30851028726761, 36.72644825274648),
                Arrays.asList(127.36479778834358, 36.68507636379607),
                Arrays.asList(127.37451707665686, 36.674527261749525),
                Arrays.asList(127.37341850557277, 36.65200735127948),
                Arrays.asList(127.38107526304393, 36.632245704244795),
                Arrays.asList(127.38303029780384, 36.627080729093386),
                Arrays.asList(127.38303029780384, 36.627080729093386),
                Arrays.asList(127.4305295251844, 36.56516799699589),
                Arrays.asList(127.4305295251844, 36.56516799699589),
                Arrays.asList(127.43210220588804, 36.55363043485985),
                Arrays.asList(127.43160697475312, 36.54286773348848),
                Arrays.asList(127.43020967328724, 36.532986405455695),
                Arrays.asList(127.42923867327536, 36.51398873766687),
                Arrays.asList(127.42794983703914, 36.471908653925546),
                Arrays.asList(127.4197184682824, 36.43852029408191),
                Arrays.asList(127.41731976383807, 36.429692944186755),
                Arrays.asList(127.41870756182243, 36.39700720499854),
                Arrays.asList(127.45811531815426, 36.355785597637855)
        );



        List<List<Double>> convertedCoords = new ArrayList<>();

        for (List<Double> coord : originalCoords) {
            double x = coord.get(0);
            double y = coord.get(1);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + REST_API_KEY);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    URL + "?x=" + x + "&y=" + y + "&input_coord=WGS84&output_coord=WTM",
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                try {
                    String responseBody = response.getBody();
                    JsonNode jsonNode = objectMapper.readTree(responseBody); // JSON 파싱
                    JsonNode documents = jsonNode.get("documents");
                    if (documents.isArray() && documents.size() > 0) {
                        JsonNode firstDocument = documents.get(0);
                        double convertedX = firstDocument.get("x").asDouble(); // 변환된 X 좌표
                        double convertedY = firstDocument.get("y").asDouble(); // 변환된 Y 좌표
                        convertedCoords.add(Arrays.asList(convertedX, convertedY));
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // 예외 처리
                }
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
            }
        }

        log.info(convertedCoords.toString());
        return ResponseEntity.ok(convertedCoords);
    }


    @GetMapping("/api/distance")
    public ResponseEntity<DistanceRequestDto> getDistance() {

        List<List<Double>> wtm = Arrays.asList(
                Arrays.asList(208930.58309994685, 433022.0632990198),
                Arrays.asList(209153.33089994552, 428837.2916989573),
                Arrays.asList(209161.19689994407, 427337.3242989341),
                Arrays.asList(209168.0141999452, 426037.3524989146),
                Arrays.asList(209172.20939994554, 425237.3698989027),
                Arrays.asList(209180.075399944, 423737.40249888133),
                Arrays.asList(209192.6609999454, 421337.4544988447),
                Arrays.asList(209197.90499994386, 420337.4761988302),
                Arrays.asList(209202.10019994402, 419537.4934988166),
                Arrays.asList(209205.770999945, 418837.5086988071),
                Arrays.asList(209208.39299994352, 418337.51949880086),
                Arrays.asList(209208.39299994352, 418337.51949880086),
                Arrays.asList(209396.97209994306, 415354.5776987551),
                Arrays.asList(209383.77319994304, 414391.024298741),
                Arrays.asList(208875.37879994637, 413529.9090987276),
                Arrays.asList(208506.504099949, 412608.2194987135),
                Arrays.asList(208506.504099949, 412608.2194987135),
                Arrays.asList(208371.193799949, 411053.4501986904),
                Arrays.asList(208530.5225999495, 409070.27599866176),
                Arrays.asList(208530.5225999495, 409070.27599866176),
                Arrays.asList(208534.31079994803, 408970.4816986597),
                Arrays.asList(208078.62739995072, 407345.729798635),
                Arrays.asList(207870.62569995198, 406783.5962986266),
                Arrays.asList(207492.88079995592, 405751.9391986113),
                Arrays.asList(207544.57629995406, 404686.2557985941),
                Arrays.asList(207544.57629995406, 404686.2557985941),
                Arrays.asList(207544.57629995406, 404686.2557985941),
                Arrays.asList(208264.61069995072, 403423.1752985753),
                Arrays.asList(209159.24289994498, 402625.2859985633),
                Arrays.asList(209924.7857999404, 401843.9368985514),
                Arrays.asList(210597.89599993677, 400070.6983985249),
                Arrays.asList(210940.03129993525, 399132.1435985104),
                Arrays.asList(211247.55939993274, 398287.29699849803),
                Arrays.asList(211593.86999992994, 397458.94489848614),
                Arrays.asList(212006.50739992774, 396352.75479846913),
                Arrays.asList(212064.96839992772, 395555.7212984562),
                Arrays.asList(212131.54609992774, 394659.11629844224),
                Arrays.asList(212305.08489992638, 393273.0306984219),
                Arrays.asList(212305.08489992638, 393273.0306984219),
                Arrays.asList(212325.59429992584, 393175.2610984198),
                Arrays.asList(212325.59429992584, 393175.2610984198),
                Arrays.asList(212325.59429992584, 393175.2610984198),
                Arrays.asList(212325.59429992584, 393175.2610984198),
                Arrays.asList(212932.0138999237, 390655.26219838206),
                Arrays.asList(212932.0138999237, 390655.26219838206),
                Arrays.asList(213131.43219992294, 390197.3139983746),
                Arrays.asList(213346.71019992017, 389638.6651983666),
                Arrays.asList(213579.19619992038, 388874.05799835455),
                Arrays.asList(213579.19619992038, 388874.05799835455),
                Arrays.asList(213608.13409991981, 388778.4435983533),
                Arrays.asList(213724.34369991822, 388396.1247983482),
                Arrays.asList(214178.50479991522, 387185.0108983279),
                Arrays.asList(215708.7616999063, 384843.6183982934),
                Arrays.asList(216692.96399990033, 383338.7550982707),
                Arrays.asList(216865.39229989998, 381883.3571982486),
                Arrays.asList(216834.87269990076, 380385.21029822575),
                Arrays.asList(216843.1246999006, 379386.27949820925),
                Arrays.asList(216785.03729990026, 375395.7296981504),
                Arrays.asList(216669.40529990243, 374504.1253981348),
                Arrays.asList(216565.57629990292, 373711.7892981232),
                Arrays.asList(215713.78769990613, 371690.4020980918),
                Arrays.asList(215148.42319991163, 369912.0953980647),
                Arrays.asList(215148.42319991163, 369912.0953980647),
                Arrays.asList(214829.0007999123, 368980.444198051),
                Arrays.asList(214741.0687999127, 367584.64579802845),
                Arrays.asList(214870.05789991136, 366802.14099801704),
                Arrays.asList(215552.49059990805, 365035.6789979902),
                Arrays.asList(215904.92939990744, 363891.10519797355),
                Arrays.asList(216317.80039990353, 363343.33339796355),
                Arrays.asList(216984.8115998991, 363169.80969796004),
                Arrays.asList(217882.586599894, 363121.67539795954),
                Arrays.asList(219460.78259988525, 362352.6624979484),
                Arrays.asList(219460.78259988525, 362352.6624979484),
                Arrays.asList(219460.78259988525, 362352.6624979484),
                Arrays.asList(220629.04829987965, 360744.0047979234),
                Arrays.asList(222571.241699867, 359814.9280979093),
                Arrays.asList(223701.12199986156, 359219.23529789876),
                Arrays.asList(225367.27019985174, 359543.2679979047),
                Arrays.asList(226576.72169984417, 359192.12779789884),
                Arrays.asList(227559.11549983744, 358699.8975978908),
                Arrays.asList(232604.74829980964, 354126.4112978196),
                Arrays.asList(233478.009399805, 352959.10109780263),
                Arrays.asList(233389.5334998055, 350459.63819776336),
                Arrays.asList(234082.87689980163, 348269.3463977291),
                Arrays.asList(234260.01959979924, 347696.87739772024),
                Arrays.asList(234260.01959979924, 347696.87739772024),
                Arrays.asList(238539.37299977557, 340844.3552976139),
                Arrays.asList(238539.37299977557, 340844.3552976139),
                Arrays.asList(238685.90579977378, 339564.65959759336),
                Arrays.asList(238646.92549977574, 338370.12229757523),
                Arrays.asList(238526.70959977646, 337273.03179755714),
                Arrays.asList(238449.15259977596, 335164.4789975248),
                Arrays.asList(238354.44719977654, 330494.3769974536),
                Arrays.asList(237632.84499978088, 326786.0819973955),
                Arrays.asList(237422.00829978363, 325805.59599737776),
                Arrays.asList(237562.19029978363, 322179.078297324),
                Arrays.asList(241119.16749976252, 317620.8896972495)
        );


        DistanceRequestDto distanceDto = distanceCalculatorService.calculateDistances(wtm);

        if (distanceDto != null) {
            return ResponseEntity.ok(distanceDto); // 계산된 거리 리스트를 반환합니다.
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 오류가 발생한 경우 500 에러를 반환합니다.
        }
    }
}

