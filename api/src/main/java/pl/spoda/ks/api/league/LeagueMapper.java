package pl.spoda.ks.api.league;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.league.enums.LeagueType;
import pl.spoda.ks.api.league.enums.TeamStructure;
import pl.spoda.ks.api.league.model.LeagueData;
import pl.spoda.ks.api.league.model.LeagueRequest;
import pl.spoda.ks.api.player.PlayerMapper;
import pl.spoda.ks.database.dto.LeagueDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeagueMapper {

    private final PlayerMapper playerMapper;

    public LeagueDto mapLeague(LeagueRequest request) {
        return LeagueDto.builder()
                .name( request.getName().toUpperCase() )
                .description( request.getDescription() )
                .teamStructure( request.getTeamStructure().name() )
                .type( request.getType().name() )
                .playerList(playerMapper.mapToPlayerDtoList( request.getPlayerList() ))
                .logoUrl( request.getLogoUrl() )
                .isPrivate(request.getIsPrivate())
                .build();
    }

    public List<LeagueData> mapLeagueList(List<LeagueDto> storedLeagues) {
        return storedLeagues.stream()
                .map( this::mapToLeagueData )
                .toList();
    }

    public LeagueData mapToLeagueData(LeagueDto leagueDto) {
        return LeagueData.builder()
                .leagueId( leagueDto.getId() )
                .description( leagueDto.getDescription() )
                .name( leagueDto.getName() )
                .type( LeagueType.getByName( leagueDto.getType()) )
                .teamStructure( TeamStructure.getByName(  leagueDto.getTeamStructure() ))
                .creationDate( leagueDto.getCreationDate() )
                .logoUrl( leagueDto.getLogoUrl() )
                .isPrivate( leagueDto.getIsPrivate() )
                .build();

    }
}
